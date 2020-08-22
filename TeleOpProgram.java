package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name = "TeleOpsBanana", group = "TeleOp")
public class TeleOpProgram extends LinearOpMode {
    BananaWheelsRobot robot = new BananaWheelsRobot();
    private static final boolean PHONE_IS_PORTRAIT = true;

    @Override
    public void runOpMode() {
        float speed = 0.0f;
        double turnspeed = 0.0f;

        robot.init(hardwareMap);

        telemetry.addData("Driver", "Waiting for you to Start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            speed = gamepad1.left_stick_y;
            robot.topLeft.setPower(-speed);
            robot.bottomLeft.setPower(-speed);
            robot.topRight.setPower(-speed);
            robot.bottomRight.setPower(-speed);

            turnspeed = gamepad1.left_stick_x;
            robot.topLeft.setPower(-turnspeed);
            robot.bottomLeft.setPower(-turnspeed);
            robot.topRight.setPower(turnspeed);
            robot.topLeft.setPower(turnspeed);

            if (gamepad1.dpad_right) {
                robot.topLeft.setPower(0.75);
                robot.bottomLeft.setPower(-0.75);
                robot.topRight.setPower(-0.75);
                robot.bottomRight.setPower(0.75);
            }

            if (gamepad1.dpad_up) {
                robot.topLeft.setPower(0.2);
                robot.bottomLeft.setPower(0.2);
                robot.topRight.setPower(0.2);
                robot.bottomRight.setPower(0.2);
            }

            if (gamepad1.dpad_left) {
                robot.topLeft.setPower(-0.75);
                robot.bottomLeft.setPower(0.75);
                robot.topRight.setPower(0.75);
                robot.bottomRight.setPower(-0.75);
            }

            if (gamepad1.dpad_down) {
                robot.topLeft.setPower(-0.2);
                robot.bottomLeft.setPower(-0.2);
                robot.topRight.setPower(-0.2);
                robot.bottomRight.setPower(-0.2);
            }


        }
    }
}