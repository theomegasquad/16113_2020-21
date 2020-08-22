package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

@Autonomous(name = "SysTestAuto", group = "Autonomous")


public class SysTestAuto extends LinearOpMode{
    BananaWheelsRobot robot = new BananaWheelsRobot();
    private ElapsedTime runtime = new ElapsedTime();

    public void runOpMode() {
        // Put initialization blocks here.
        robot.init(hardwareMap);

        telemetry.addData("Autonomous Mode Status", "Ready to Run");
        telemetry.update();

        waitForStart();

        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.4)) {
            robot.topLeft.setPower(0.5);
            robot.bottomLeft.setPower(0.5);
            robot.topRight.setPower(0.5);
            robot.bottomRight.setPower(0.5);

        }

        StopSteering();

        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 1.4)) {
            robot.topLeft.setPower(-0.5);
            robot.bottomLeft.setPower(-0.5);
            robot.topRight.setPower(-0.5);
            robot.bottomRight.setPower(-0.5);

        }

        StopSteering();

        while (opModeIsActive() && (runtime.seconds() < 1.4)) {
            robot.topLeft.setPower(0.75);
            robot.bottomLeft.setPower(-0.75);
            robot.topRight.setPower(-0.75);
            robot.bottomRight.setPower(0.75);

        }

        StopSteering();
    }


    private void StopSteering() {
        robot.topLeft.setPower(0);
        robot.bottomLeft.setPower(0);
        robot.topRight.setPower(0);
        robot.bottomRight.setPower(0);
    }
}
