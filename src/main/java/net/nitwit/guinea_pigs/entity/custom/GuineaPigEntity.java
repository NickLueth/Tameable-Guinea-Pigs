package net.nitwit.guinea_pigs.entity.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jspecify.annotations.Nullable;

import net.nitwit.guinea_pigs.entity.ModEntities;
import net.nitwit.guinea_pigs.item.ModItems;
import net.nitwit.guinea_pigs.sound.ModSounds;


public class GuineaPigEntity extends TamableAnimal {

    // Timers and animation state trackers
    public int poopTime = this.random.nextInt(2000) + 2000;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState sittingAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    private int ambientSoundCooldown = this.random.nextInt(1000, 1800);
    private static final Ingredient FAVORITE_FOODS = Ingredient.of(
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
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT =
            SynchedEntityData.defineId(GuineaPigEntity.class, EntityDataSerializers.INT);

    public GuineaPigEntity(EntityType<? extends GuineaPigEntity> entityType, Level level) {
        super(entityType, level);
    }

    // Defines behavior goals (AI)
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Creeper.class, 6.0F, 1.0D, 1.5D));
        this.goalSelector.addGoal(3, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new BreedGoal(this, 1.25));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.25, FAVORITE_FOODS, false));
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(10, new EatBlockGoal(this));
    }

    // Attribute setup for health, speed, and follow range
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, .25)
                .add(Attributes.FOLLOW_RANGE, 20.0);
    }

    // Handles animations each tick (client-side only)
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if (this.isTame()) {
            if (this.isInSittingPose()) {
                this.sittingAnimationState.startIfStopped(this.tickCount);
            } else {
                this.sittingAnimationState.stop();
            }
        }
    }

    // Called every tick
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        } else {
            // Drop droppings periodically
            if (this.isAlive() && --this.poopTime <= 0) {
                this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

                if (this.level() instanceof ServerLevel serverLevel) {
                    this.spawnAtLocation(serverLevel, ModItems.DROPPINGS);
                }

                this.gameEvent(GameEvent.ENTITY_PLACE);
                this.poopTime = this.random.nextInt(2000) + 2000;
            }

            // Chutting ambient sound (periodic)
            if (--ambientSoundCooldown <= 0 && this.isAlive()) {
                this.playSound(this.getAmbientSound(), this.getSoundVolume(), this.getVoicePitch());
                ambientSoundCooldown = this.random.nextInt(1000, 1800);
            }
        }
    }

    // Defines which items can breed guinea pigs
    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.DANDELION);
    }

    // Taming logic (20% chance on feeding favorite foods)
    private void tryTame(Player player) {
        if (this.random.nextInt(5) == 0) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget(null);
            this.setTarget(null);
            this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, EntityEvent.TAMING_SUCCEEDED);
        } else {
            this.level().broadcastEntityEvent(this, EntityEvent.TAMING_FAILED);
        }
    }

    // Right-click interaction logic
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.level().isClientSide() || this.isBaby() && (this.isFood(itemStack) || FAVORITE_FOODS.test(itemStack))) {
            if (!itemStack.is(Items.DANDELION) && FAVORITE_FOODS.test(itemStack)) {
                // Heal or tame with foods
                if (this.isTame() && this.getHealth() < this.getMaxHealth()) {
                    this.feed(player, hand, itemStack, 2.0F, 2.0F);
                    if (!this.level().isClientSide()) {
                        this.level().broadcastEntityEvent(this, EntityEvent.TRUSTING_SUCCEEDED);
                    }
                    this.playSound(ModSounds.WHEEK);
                    return InteractionResult.SUCCESS;

                } else if (!this.isTame()) {
                    this.usePlayerItem(player, hand, itemStack);
                    this.tryTame(player);
                    this.playSound(ModSounds.WHEEK);
                    return InteractionResult.SUCCESS;
                }

            } else if (itemStack.is(Items.DANDELION)) {

                // Trigger breeding with dandelion
                this.usePlayerItem(player, hand, itemStack);
                this.setInLove(player);
                this.playSound(ModSounds.RUMBLING);
                return InteractionResult.SUCCESS;
            }

            // Sit toggle if owned
            InteractionResult interactionResult = super.mobInteract(player, hand);

            if (!interactionResult.consumesAction() && this.isOwnedBy(player)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS.withoutItem();
            }

            return interactionResult;

        } else {
            boolean bl = this.isOwnedBy(player) || this.isTame() || FAVORITE_FOODS.test(itemStack) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
    }

    // Handle client-side entity status (like particles)
    @Override
    public void handleEntityEvent(final @EntityEvent.Value byte id) {
        if (id == EntityEvent.TRUSTING_SUCCEEDED) {
            for (int i = 0; i < 7; ++i) {
                double dx = this.random.nextGaussian() * 0.02D;
                double dy = this.random.nextGaussian() * 0.02D;
                double dz = this.random.nextGaussian() * 0.02D;
                this.level().addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(1.0D),
                        this.getRandomY() + 0.5D,
                        this.getRandomZ(1.0D),
                        dx, dy, dz
                );
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    // Spawning baby guinea pig
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        GuineaPigEntity baby =
                ModEntities.GUINEA_PIG.create(level, EntitySpawnReason.BREEDING);
        if (baby != null) {
            GuineaPigVariant variant =
                    GuineaPigVariant.values()[this.random.nextInt(GuineaPigVariant.values().length)];
            baby.setVariant(variant);
            this.playSound(ModSounds.RUMBLING);
        }

        return baby;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isInSittingPose() ? 20 : super.getMaxHeadXRot();
    }

    // Reset sitting pose on taking damage
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isInvulnerableTo(level, source)) return false;
        this.setOrderedToSit(false);
        return super.hurtServer(level, source, damage);
    }

    // Sound definitions
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CHUTTING;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SCREAM;
    }

    // Data tracker initialization
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_TYPE_VARIANT, 9); // Default to Acorn Squash (id 9)
    }

    // Sitting logic
    public boolean isSitting() {
        return this.isInSittingPose();
    }

    // Variant logic
    public GuineaPigVariant getVariant() {
        return GuineaPigVariant.byId(this.getTypeVariant() & 255);
    }

    private int getTypeVariant() {
        return this.entityData.get(DATA_ID_TYPE_VARIANT);
    }

    private void setVariant(GuineaPigVariant variant) {
        this.entityData.set(DATA_ID_TYPE_VARIANT, variant.getId() & 255);
    }

    // Save guinea pig data
    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("Variant", this.getTypeVariant());
        output.putBoolean("IsSitting", this.isSitting());
    }

    // Load guinea pig data
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(
                DATA_ID_TYPE_VARIANT,
                input.getIntOr("Variant", 9)
        );

        // Read the old 1.21.1 sitting field for compatibility.
        if (input.getBooleanOr("IsSitting", false)) {
            this.setOrderedToSit(true);
        }
    }

    // Randomize variant when spawning
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        GuineaPigVariant variant = GuineaPigVariant.values()[this.random.nextInt(GuineaPigVariant.values().length)];
        this.setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }
}