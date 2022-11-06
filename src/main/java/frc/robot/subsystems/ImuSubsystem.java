package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.math.geometry.Rotation2d;
// import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.simulation.ADXRS450_GyroSim;
import frc.robot.Constants.DriveConstants;

public class ImuSubsystem extends SubsystemBase {
    private PigeonIMU pidgey;
    private ADXRS450_Gyro gyro;
    private ADXRS450_GyroSim simGyro;

    public ImuSubsystem(PigeonIMU pImu) {
        if (RobotBase.isReal()) {
            pidgey = pImu;
        } else {
            gyro = new ADXRS450_Gyro();
            simGyro = new ADXRS450_GyroSim(gyro);
        }
    }
    

    @Override
    public void periodic() {

    }

    public Rotation2d getYaw() {
        double[] ypr = new double[3];
        pidgey.getYawPitchRoll(ypr);
        return (DriveConstants.invertGyro) ? Rotation2d.fromDegrees(360 - ypr[0]) : Rotation2d.fromDegrees(ypr[0]);
    }

    public double getAngle() {
            
            double[] ypr_deg = new double[3];
            pidgey.getYawPitchRoll(ypr_deg);
            return ypr_deg[0];
       
        
    }





    public void resetGyro(){
        if (RobotBase.isReal()) {
            pidgey.setYaw(0.0);
        } else {
            gyro.reset();
        }
        
    }

    public void setAngle(double degrees) {
        if (RobotBase.isSimulation()) {
            simGyro.setAngle(-1*degrees);
        }
    }
}