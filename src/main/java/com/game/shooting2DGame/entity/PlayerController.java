package com.game.shooting2DGame.entity;

import com.game.shooting2DGame.config.PlayerConfig;
import com.game.shooting2DGame.input.KeyBoardInputs;
import com.game.shooting2DGame.input.MouseInputs;
import com.game.shooting2DGame.projectiles.Laser;
import com.game.shooting2DGame.projectiles.Projectile;
import com.game.shooting2DGame.projectiles.ProjectileManager;
import com.game.shooting2DGame.render.Camera;
import com.game.shooting2DGame.sound.SoundConstants;
import com.game.shooting2DGame.sound.SoundManager;
import com.game.shooting2DGame.state.PlayerState;
import com.game.shooting2DGame.utils.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PlayerController {
    private final KeyBoardInputs keyBoardInputs;
    private final MouseInputs mouseInputs;
    private final Player player;
    private final Camera camera;
    private Laser activeLaser;

    public PlayerController(KeyBoardInputs keyBoardInputs, MouseInputs mouseInputs,
                            Camera camera, Player player) {
        this.keyBoardInputs = keyBoardInputs;
        this.mouseInputs = mouseInputs;
        this.camera = camera;
        this.player = player;
    }

    public void update(ProjectileManager projectileManager) {
        player.setBoundPos(camera.getScreenPos(player.getPosition()));
        if (player.getPlayerState() == PlayerState.BURSTING_LASER) {
            if (activeLaser == null || !activeLaser.isActive()) {
                activeLaser = null;
                player.setState(PlayerState.NORMAL);
            }
        }
        Vector2D screenPos = camera.getScreenPos(player.getPosition());
        Vector2D direction = mouseInputs.getMousePos().subtract(screenPos);
        double viewAngle = Math.atan2(direction.getY(), direction.getX()) + Math.PI / 2.0;
        player.setViewAngle(viewAngle);
        handleState(viewAngle);
        if (mouseInputs.isMousePressed(MouseEvent.BUTTON3)) {
            player.switchWeapon();
            Projectile projectile = player.shoot(direction);
            if (projectile != null) {
                SoundManager.getInstance().playSfx(SoundConstants.SFX_LASER);
                activeLaser = (Laser)projectile;
                player.setState(PlayerState.BURSTING_LASER);
                ((Laser)projectile).setShootAngle(player::getViewAngle);
                ((Laser)projectile).setShootPos(player::getPosition);
                projectileManager.addActiveProjectile(projectile);
            }
            player.switchWeapon();
        }
        if (mouseInputs.isMousePressed(MouseEvent.BUTTON1)
              && player.getPlayerState() != PlayerState.BURSTING_LASER) {
            Projectile projectile = player.shoot(direction);
            if (projectile != null) {
                SoundManager.getInstance().playSfx(SoundConstants.SFX_PLASMA);
                projectile.setBoundPos(camera.getScreenPos(projectile.getPosition()));
                projectileManager.addActiveProjectile(projectile);
            }
        }

    }

    private void handleState(double viewAngle) {
        if (player.getPlayerState() == PlayerState.BURSTING_LASER) {
            player.dragged(4 * PlayerConfig.DRAG);
            return;
        }
        Vector2D moveDir = handleMovement();
        if (player.getPlayerState() == PlayerState.BOOSTING) {
            player.setTargetAngle(viewAngle);
            return;
        }
        if (player.getPlayerState() == PlayerState.BOOST_DECAY) {
            if (!moveDir.equals(Vector2D.ZERO)) {
                double targetAngle = Math.atan2(moveDir.getY(), moveDir.getX()) + Math.PI / 2.0;
                player.setTargetAngle(targetAngle);
            }
            return;
        }
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_SHIFT) && player.shouldBoost()) {
            player.setTargetAngle(viewAngle);
            player.startBoost();
            SoundManager.getInstance().playSfx(SoundConstants.SFX_BOOST);
        } else {
            player.setState(PlayerState.NORMAL);
            if (!moveDir.equals(Vector2D.ZERO)) {
                player.setDirection(moveDir);
                player.accelerate(PlayerConfig.ACCELERATION);
            } else player.dragged(PlayerConfig.DRAG);
        }
    }

    private Vector2D handleMovement() {
        int dx = 0;
        int dy = 0;
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_A)
              || keyBoardInputs.isKeyPressed(KeyEvent.VK_LEFT)) {
            dx -= 1;
        }
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_D)
              || keyBoardInputs.isKeyPressed(KeyEvent.VK_RIGHT)) {
            dx += 1;
        }
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_W)
              || keyBoardInputs.isKeyPressed(KeyEvent.VK_UP)) {
            dy -= 1;
        }
        if (keyBoardInputs.isKeyPressed(KeyEvent.VK_S)
              || keyBoardInputs.isKeyPressed(KeyEvent.VK_DOWN)) {
            dy += 1;
        }
        return new Vector2D(dx, dy);
    }



}
