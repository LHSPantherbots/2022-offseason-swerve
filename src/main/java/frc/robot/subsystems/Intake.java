package frc.robot.subsystems;

import frc.robot.Constants.RIO_Channels_CAN_MOTOR;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;



public class Intake extends SubsystemBase{
    


    // DigitalInput DownS = new DigitalInput(2);
    // DigitalInput UpS = new DigitalInput(0);

    

  
 
    //TalonSRX talonIntake = new TalonSRX(20);
    TalonSRX talonIntake;
    
    DoubleSolenoid intakeSolenoid = new DoubleSolenoid(RIO_Channels_CAN_MOTOR.PH, PneumaticsModuleType.REVPH, 0, 1);
    // Solenoid intakeSolenoid = new Solenoid(PneumaticsModuleType.REVPH, 0);

  public Intake(TalonSRX tSrx) {
      talonIntake = tSrx;
      talonIntake.setInverted(true);
      
  }




    @Override
    public void periodic() {
    // SmartDashboard.putBoolean("Intake Sensor", IsBallD());
    // SmartDashboard.putBoolean("Launcher Sensor", IsBallU());

    }

    public void run() {
        
        talonIntake.set(ControlMode.PercentOutput, .8);
    }

    public void stop() {
        
        talonIntake.set(ControlMode.PercentOutput, 0);
    }


   

    // public boolean IsBallD()
    // {   
    //     return DownS.get();
    // }

    // public boolean IsBallU()
    // {
    //     return UpS.get();
    // }
    
    // public void BallOBall()
    // {
    //     if(IsBallU()&& !IsBallD())
    //     {
    //         talonIntake.set(ControlMode.PercentOutput, -.7);
    //     }
    //     else
    //     {
    //         talonIntake.set(ControlMode.PercentOutput,0);
    //     }
    // }

    // public void IntakeControl()
    // {
    //     if (IsBallU())
    //     {
    //         talonIntake.set(ControlMode.PercentOutput,0);
    //         // intakeSolenoid.set(false);
    //         intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    //     }
    //     else
    //     {
    //         talonIntake.set(ControlMode.PercentOutput,.8);
    //         // intakeSolenoid.set(true);
    //         intakeSolenoid.set(DoubleSolenoid.Value.kForward);
    //     }
    //     return;
    // }

    public void intakeUp()
    {
        //intakeSolenoid.set(false);
        intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void intakeDown()
    {
        // intakeSolenoid.set(true);
        intakeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void intakeDownRoll(){
        intakeDown();
        intakeRollersForward();
    }

    public void intakeUpStop(){
        intakeUp();
        intakeRollersOff();
    }

    public void toggleIntakePosition()
    {
        // intakeSolenoid.toggle();
        intakeSolenoid.toggle();
    }

    public void intakeRollersForward()
    {
        talonIntake.set(ControlMode.PercentOutput,1);
    }

    public void intakeRollersOff()
    {
        talonIntake.set(ControlMode.PercentOutput,0);
    }

    public void intakeRollersReverse()
    {
        talonIntake.set(ControlMode.PercentOutput,-.5);
    }

    
    // public void SensCanon(){
    //     if(IsBallU())
    //     {
    //         talonIntake.set(ControlMode.PercentOutput,-.7);
    //     }
    //     else
    //     {
    //         talonIntake.set(ControlMode.PercentOutput,0);
    //     }
    //  }
        

}




