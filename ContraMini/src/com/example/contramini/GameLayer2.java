package com.example.contramini;

import java.util.ArrayList;
import java.util.Iterator;

import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCParallaxNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.view.MotionEvent;

public class GameLayer2 extends CCLayer{
	CCSprite player;
	CCSprite landMonster1;
	CCSprite landMonster2;
	CCSprite back;
	CCParallaxNode backgroundNode;
	
	//projectile array
	ArrayList<CCSprite> projectileArray;
	
	//monster array
	ArrayList<CCSprite> monsterArray;
	
	//control buttons
	CCSprite leftButton;
	CCSprite rightButton;
	CCSprite jumpButton;
	CCSprite shootButton;
	
	
	public GameLayer2(){
		// enable touch operation
		this.setIsTouchEnabled(true);
		//set control buttons
		leftButton = CCSprite.sprite("leftbutton.png");
		rightButton = CCSprite.sprite("rightbutton.png");
		jumpButton = CCSprite.sprite("jumpW.png");
		shootButton = CCSprite.sprite("shootW.png");
		
		projectileArray = new ArrayList<CCSprite>();
		monsterArray = new ArrayList<CCSprite>();
		
		CGPoint LBPosition = CGPoint.ccp(250, 150);
		CGPoint RBPosition = CGPoint.ccp(750, 150);
		CGPoint JBPosition = CGPoint.ccp(1500, 150);
		CGPoint SBPosition = CGPoint.ccp(1500, 600);
		
		leftButton.setPosition(LBPosition);
		rightButton.setPosition(RBPosition);
		jumpButton.setPosition(JBPosition);
		shootButton.setPosition(SBPosition);
		
		leftButton.setOpacity(100);
		rightButton.setOpacity(100);
		jumpButton.setOpacity(100);
		shootButton.setOpacity(100);
		
		//add control buttons
		
		this.addChild(leftButton,2);
		this.addChild(rightButton,2);
		this.addChild(jumpButton,2);
		this.addChild(shootButton,2);
		// set the player
		player = CCSprite.sprite("contrachar.png");
		player.setPosition(100, 70);
		player.setScale(0.3f);
		
		// set monster
		landMonster1 = CCSprite.sprite("goomba2.png");
		landMonster1.setScale(0.3f);
		landMonster2 = CCSprite.sprite("goomba2.png");
		landMonster2.setScale(0.3f);
		back = CCSprite.sprite("long.jpg");
		back.setScale(2.3f);
		backgroundNode = CCParallaxNode.node();
		CGSize winsize = CCDirector.sharedDirector().winSize();
		
		
		float backHeight = back.getBoundingBox().size.height;
		float backWidth = back.getBoundingBox().size.width;
		
		float landMonsterWidth = landMonster1.getBoundingBox().size.width;
		float landMonsterHeight =landMonster1.getBoundingBox().size.height;
		
		// 3) Determine relative movement speeds for space dust and background
		CGPoint monsterSpeed = CGPoint.ccp(0.1f, 0.1f);
		CGPoint bgSpeed = CGPoint.ccp(0.05f, 0.05f);
		
		backgroundNode.addChild(back, -1, 0.05f, 0.05f, backWidth/2, backHeight/2);
		backgroundNode.addChild(landMonster1, 0, 0.1f, 0.1f, 600f, landMonsterHeight/2);
		backgroundNode.addChild(landMonster2, 0, 0.1f, 0.1f, 4000f, landMonsterHeight/2);
		this.addChild(player, 0);
		this.addChild(backgroundNode, -1);
		
		//add monsters to monster array
		monsterArray.add(landMonster1);
		monsterArray.add(landMonster2);
		
		this.schedule("update");
		
	}
	
	@Override
	public boolean ccTouchesBegan(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		CGPoint p1 = CGPoint.ccp(x, y);
		CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
		System.out.println("p1.x:"+ x + ".p1.y:" + y);
		System.out.println("p2.x:"+ p2.x + ".p2.y:" + p2.y);
		System.out.println("began");
		
		float playerX =player.getPosition().x;
		//jump action
		CGPoint jumpUpVec = CGPoint.ccp(0, 200);
		CGPoint jumpDownDestination = CGPoint.ccp(playerX, 70);
		CCMoveBy moveUp = CCMoveBy.action(0.5f, jumpUpVec);
		CCMoveTo moveDown = CCMoveTo.action(0.5f, jumpDownDestination);
		CCSequence jumpSec = CCSequence.actions(moveUp, moveDown);
		
		
		if(x > 1000 && y > 500){   //jump
			player.runAction(jumpSec);

		}else if(x >1000 && y < 500){   //shoot
			this.shoot();
		}
		return super.ccTouchesBegan(event);
	}

	@Override
	public boolean ccTouchesMoved(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		CGPoint p1 = CGPoint.ccp(x, y);
		CGPoint p2 = CCDirector.sharedDirector().convertToGL(p1);
		System.out.println("p1.x:"+ x + ".p1.y:" + y);
		System.out.println("p2.x:"+ p2.x + ".p2.y:" + p2.y);
		System.out.println("began");
		
		float realX = p2.x;
		float realY = p2.y;
		
		CGPoint moveForwardVetor = CGPoint.ccp(50, 0);
		CGPoint moveBackwardVector = CGPoint.ccp(-50, 0);
		
		
		CCMoveBy forwardMove = CCMoveBy.action(0.2f, moveForwardVetor);
		CCMoveBy backwardMove = CCMoveBy.action(0.2f, moveBackwardVector);
		
		CGPoint deltaLeft = CGPoint.ccp(-100, 0);
	//	CGPoint deltaRight = CGPoint.ccp(100,0);
		CGPoint updateLeft = CGPoint.ccpAdd(backgroundNode.getPosition(), deltaLeft);
	//	CGPoint updateRight = CGPoint.ccpAdd(backgroundNode.getPosition(), deltaRight);
//		if(x >1000){
//			backgroundNode.setPosition(updateLeft);
//		}else{
//			backgroundNode.setPosition(updateRight);
//		}
		if(realX <= 500){   //back move
			player.runAction(backwardMove);
			
		}else if(realX<= 1000){   //forward move
			float playerPositionX = player.getPosition().x;
			if(playerPositionX < 1000){
				player.runAction(forwardMove);
			}else{
				backgroundNode.setPosition(updateLeft);
			}
		}

		return super.ccTouchesMoved(event);
	}
	
	public void shoot(){
		float x =player.getPosition().x;
		float y =player.getPosition().y;
		CCSprite projectile = CCSprite.sprite("Projectile.png");
		this.addChild(projectile);
		CGPoint ini = CGPoint.ccp(x, y);
		projectile.setPosition(ini);
		this.projectileArray.add(projectile);
		
		CGPoint target = CGPoint.ccp(1800, y);
		CCMoveTo moveProjec = CCMoveTo.action(0.5f, target);
		projectile.runAction(moveProjec);

		
		
	}
	
	public void update(float dt){
		Iterator<CCSprite> proIterator = this.projectileArray.iterator();
		while(proIterator.hasNext()){
			CCSprite projectile = proIterator.next();
			if(projectile.getPosition().x > 1700){
				this.removeChild(projectile, true);
				proIterator.remove();
			}
		}
		

		Iterator<CCSprite> monIterator = this.monsterArray.iterator();
		while(monIterator.hasNext()){
			CCSprite monster = monIterator.next();
			CGRect monsterRect = CGRect.make(monster.getPosition().x - (monster.getContentSize().width / 2.0f),
					monster.getPosition().y - (monster.getContentSize().height / 2.0f),
					monster.getContentSize().width,
					monster.getContentSize().height);
			for(CCSprite projectile:this.projectileArray){
				CGRect projectileRect = CGRect.make(projectile.getPosition().x - (projectile.getContentSize().width / 2.0f),
	                    projectile.getPosition().y - (projectile.getContentSize().height / 2.0f),
	                    projectile.getContentSize().width,
	                    projectile.getContentSize().height);
			

				if(CGRect.intersects(monsterRect, projectileRect)){
					System.out.println("SSSSSSSSSSSSSSSS");
					monster.removeSelf();
					monIterator.remove();
				
			}
		}
		}
		
		
		
		
		
	}

}