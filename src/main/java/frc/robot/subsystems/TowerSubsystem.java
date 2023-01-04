// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.ejml.ops.ConvertMatrixData;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.RIO_Channels_CAN_MOTOR;
import frc.robot.Constants.RIO_Channels_DIO;

public class TowerSubsystem extends SubsystemBase {
  CANSparkMax towerFrontRoller = new CANSparkMax(RIO_Channels_CAN_MOTOR.TOWER_FRONT_ROLLER, MotorType.kBrushless);
  CANSparkMax towerBackRoller = new CANSparkMax(RIO_Channels_CAN_MOTOR.TOWER_BACK_ROLLER, MotorType.kBrushless);
  CANSparkMax towerBelts = new CANSparkMax(RIO_Channels_CAN_MOTOR.TOWER_BELTS, MotorType.kBrushless);
  CANSparkMax conveyor = new CANSparkMax(RIO_Channels_CAN_MOTOR.CONVEYOR, MotorType.kBrushless);
  DigitalInput upperBeamBreak = new DigitalInput(RIO_Channels_DIO.UPPER_BEAM_BREAK);
  DigitalInput lowerBeamBreak = new DigitalInput(RIO_Channels_DIO.LOWER_BEAM_BREAK);


  /** Creates a new TowerSubsystem. */
  public TowerSubsystem() {
    towerFrontRoller.restoreFactoryDefaults();
    towerBackRoller.restoreFactoryDefaults();
    towerBelts.restoreFactoryDefaults();
    conveyor.restoreFactoryDefaults();

    towerBelts.setOpenLoopRampRate(.7);
    towerFrontRoller.setOpenLoopRampRate(.7);
    towerBackRoller.setOpenLoopRampRate(.7);
    conveyor.setOpenLoopRampRate(.7);

    towerFrontRoller.setSmartCurrentLimit(20);
    towerBelts.setSmartCurrentLimit(30);
    towerBackRoller.setSmartCurrentLimit(20);
    conveyor.setSmartCurrentLimit(20);
    
    
    towerBelts.setInverted(true);
    towerFrontRoller.setInverted(false);
    towerBackRoller.setInverted(false);
    conveyor.setInverted(true);



  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putBoolean("Ball Det. Upper Sensor", isBallDetectedUpperSensor());
    SmartDashboard.putBoolean("Ball Det. Lower Sensor", isBallDetectedLowerSensor());
  }

  public boolean isBallDetectedUpperSensor(){
    return !upperBeamBreak.get();
  }

  public boolean isBallDetectedLowerSensor(){
    return !lowerBeamBreak.get();
  }

  public void feedBallsToShooter(){
    towerBackRoller.set(.5);
    towerFrontRoller.set(.5);
    towerBelts.set(.5);
    conveyor.set(.2);
  }

  public void towerStop(){
    towerBackRoller.set(0.0);
    towerFrontRoller.set(0.0);
    towerBelts.set(0.0);
    conveyor.set(0.0);
  }

  public void autoTowerStow(){
    if(!isBallDetectedUpperSensor()){
      feedBallsToShooter();
    }else{
      if(!isBallDetectedLowerSensor()){
        towerBackRoller.set(0.0);
        towerFrontRoller.set(.5);
        towerBelts.set(0.0);
        conveyor.set(.2);
      }else{
        towerStop();
      }
    }
  }

}
