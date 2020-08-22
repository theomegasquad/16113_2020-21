package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BananaWheelsRobot {

    public DcMotor bottomRight = null;
    public DcMotor topRight = null;
    public DcMotor topLeft = null;
    public DcMotor bottomLeft = null;

    HardwareMap hardwareMap = null;


    public BananaWheelsRobot() {

    }

    public void init(HardwareMap omegaSquadHardwareMap) {
        hardwareMap = omegaSquadHardwareMap;

        //Initialize all DC Motors
        bottomRight = hardwareMap.dcMotor.get("bottomRight");
        topLeft = hardwareMap.dcMotor.get("topLeft");
        topRight = hardwareMap.dcMotor.get("topRight");
        bottomLeft = hardwareMap.dcMotor.get("bottomLeft");


        topLeft.setDirection(DcMotor.Direction.FORWARD);
        bottomLeft.setDirection(DcMotor.Direction.FORWARD);
        topRight.setDirection(DcMotor.Direction.REVERSE);
        bottomRight.setDirection(DcMotor.Direction.REVERSE);

        //Set all DC Motors power to ZERO
        topLeft.setPower(0);
        bottomLeft.setPower(0);
        topRight.setPower(0);
        bottomRight.setPower(0);


        //Set all motors to run without encoders
        topLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bottomLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        topRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bottomRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }
}