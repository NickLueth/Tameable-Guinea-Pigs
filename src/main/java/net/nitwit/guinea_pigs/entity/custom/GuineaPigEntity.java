package net.nitwit.guinea_pigs.entity.custom;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.nitwit.guinea_pigs.entity.ModEntities;
import net.nitwit.guinea_pigs.item.ModItems;
import net.nitwit.guinea_pigs.sound.ModSounds;
import org.jetbrains.annotations.Nullable;


public class GuineaPigEntity extends TameableEntity {

    // Timers and animation state trackers
    public int poopTime = this.random.nextInt(2000) + 2000;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int ambientSoundCooldown = this.random.nextBetween(1000, 1800);
    private static final Ingredient FAVORITE_FOODS = Ingredient.ofItems(
            Items.DANDELION,
            Items.WHEAT,
            Items.APPLE,
            Items.CARROT,
            Items.MELON_SLICE,
            Items.SWEET_BERRIES,
            Items.GOLDEN_CARROT,
            Items.GOLDEN_APPLE,
            Items.ENCHANTED_GOLDEN_APPLE,
            Items.GLISTERING_MELON_SLICE
    );


    // Tracked data: Variant and Sitting status
    private static final TrackedData<Integer> DATA_ID_TYPE_VARIANT =
            DataTracker.registerData(GuineaPigEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SITTING =
            DataTracker.registerData(GuineaPigEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public GuineaPigEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
    }

    // Defines behavior goals (AI)
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5));
        this.goalSelector.add(2, new FleeEntityGoal<>(this, CreeperEntity.class, 6.0F, 1.0D, 1.5D));
        this.goalSelector.add(3, new SitGoal(this));
        this.goalSelector.add(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.add(5, new AnimalMateGoal(this, 1.25));
        this.goalSelector.add(6, new TemptGoal(this, 1.25, FAVORITE_FOODS, false));
        this.goalSelector.add(7, new FollowParentGoal(this, 1.25));
        this.goalSelector.add(8, new WanderAroundGoal(this, 1));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.goalSelector.add(10, new EatGrassGoal(this));
    }

    // Attribute setup for health, speed, and follow range
    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 5)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .25f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20);
    }

    // Handles animations each tick (client-side only)
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isTamed()) {
            if (this.isInSittingPose()) {
                this.sittingAnimationState.startIfNotRunning(this.age);
            } else {
                this.sittingAnimationState.stop();
            }
        }
    }

    // Called every tick
    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        } else {
            // Drop droppings periodically
            if (this.isAlive() && --this.poopTime <= 0) {
                this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.dropItem(ModItems.DROPPINGS);
                this.emitGameEvent(GameEvent.ENTITY_PLACE);
                this.poopTime = this.random.nextInt(2000) + 2000;
            }

            // Chutting ambient sound (periodic)
            if (--ambientSoundCooldown <= 0 && this.isAlive()) {
                this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getSoundPitch());
                ambientSoundCooldown = this.random.nextBetween(1000, 1800);
            }
        }
    }

    // Defines which items can breed guinea pigs
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.DANDELION);
    }

    // Taming logic (33% chance on feeding favorite foods)
    private void tryTame(PlayerEntity player) {
        if (this.random.nextInt(3) == 0) {
            this.setOwner(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setSitting(true);
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
        }
    }

    // Right-click interaction logic
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean consumed = false;

        if (!this.getWorld().isClient || this.isBaby() && (this.isBreedingItem(itemStack) || FAVORITE_FOODS.test(itemStack))) {
            if (!itemStack.isOf(Items.DANDELION) && FAVORITE_FOODS.test(itemStack)) {
                // Heal or tame with foods
                if (this.isTamed() && this.getHealth() < this.getMaxHealth()) {
                    itemStack.decrementUnlessCreative(1, player);
                    this.heal(2.0F);
                    consumed = true;

                    if (!this.getWorld().isClient()) {
                        this.getWorld().sendEntityStatus(this, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
                    }
                } else if (!this.isTamed()) {
                    itemStack.decrementUnlessCreative(1, player);
                    this.tryTame(player);
                    consumed = true;
                }

                if (consumed) this.playSound(ModSounds.WHEEK);
                return ActionResult.success(this.getWorld().isClient());
            } else if (itemStack.isOf(Items.DANDELION)) {
                // Trigger breeding with dandelion
                itemStack.decrementUnlessCreative(1, player);
                this.lovePlayer(player);
                this.playSound(ModSounds.RUMBLING);
                return ActionResult.SUCCESS;
            }

            // Sit toggle if owned
            ActionResult actionResult = super.interactMob(player, hand);
            if (!actionResult.isAccepted() && this.isOwner(player)) {
                this.setSitting(!this.isSitting());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return ActionResult.SUCCESS_NO_ITEM_USED;
            }

            return super.interactMob(player, hand);
        } else {
            boolean bl = this.isOwner(player) || this.isTamed() || FAVORITE_FOODS.test(itemStack) && !this.isTamed();
            return bl ? ActionResult.CONSUME : ActionResult.PASS;
        }
    }

    // Handle client-side entity status (like particles)
    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES) {
            for (int i = 0; i < 7; ++i) {
                double dx = this.random.nextGaussian() * 0.02D;
                double dy = this.random.nextGaussian() * 0.02D;
                double dz = this.random.nextGaussian() * 0.02D;
                this.getWorld().addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.getParticleX(1.0D),
                        this.getRandomBodyY() + 0.5D,
                        this.getParticleZ(1.0D),
                        dx, dy, dz
                );
            }
        } else {
            super.handleStatus(status);
        }
    }

    // Spawning baby guinea pig
    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        GuineaPigEntity baby = ModEntities.GUINEA_PIG.create(world);
        GuineaPigVariant variant = Util.getRandom(GuineaPigVariant.values(), this.random);
        assert baby != null;
        baby.setVariant(variant);
        this.playSound(ModSounds.RUMBLING);
        return baby;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public int getMaxLookPitchChange() {
        return this.isInSittingPose() ? 20 : super.getMaxLookPitchChange();
    }

    // Reset sitting pose on taking damage
    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        if (!this.getWorld().isClient) this.setSitting(false);
        return super.damage(source, amount);
    }

    // Sound definitions
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CHUTTING;
    }

    @Override
    public int getMinAmbientSoundDelay() {
        return 1000;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SCREAM;
    }

    // Data tracker initialization
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(DATA_ID_TYPE_VARIANT, 9); // Default to Acorn Squash (id 9)
        builder.add(SITTING, false);
    }

    // Sitting logic
    @Override
    public void setSitting(boolean sitting) {
        super.setSitting(sitting);
        this.dataTracker.set(SITTING, sitting);
        this.setInSittingPose(sitting);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    // Variant logic
    public GuineaPigVariant getVariant() {
        return GuineaPigVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.dataTracker.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GuineaPigVariant variant) {
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    // Save guinea pig data
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Variant", this.getTypeVariant());
        nbt.putBoolean("IsSitting", this.isSitting());
    }

    // Load guinea pig data
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.dataTracker.set(DATA_ID_TYPE_VARIANT, nbt.getInt("Variant"));
        this.setSitting(nbt.getBoolean("IsSitting"));
    }

    // Randomize variant when spawning
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason,
                                 @Nullable EntityData entityData) {
        GuineaPigVariant variant = Util.getRandom(GuineaPigVariant.values(), this.random);
        setVariant(variant);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }
}