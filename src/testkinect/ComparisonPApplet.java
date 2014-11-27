package testkinect;

import processing.core.PApplet;
import processing.core.PVector;
import SimpleOpenNI.SimpleOpenNI;

public class ComparisonPApplet extends PApplet {
	SimpleOpenNI context;
	SkeletonOrientations skeleton = new SkeletonOrientations();
	long time = System.currentTimeMillis();

	public void setup() {

		// TODO read model skeleton from a file
		
		context = new SimpleOpenNI(this);

		// enable depthMap generation
		context.enableDepth();
		
		context.setMirror(true);// ???

		// enable skeleton generation for all joints
		context.enableUser(SimpleOpenNI.SKEL_PROFILE_ALL);

		size(context.depthWidth(), context.depthHeight());
	}

	public void draw() {
		context.update();
		image(context.depthImage(),0,0);

		int i = 0;
		
		for (i = 0; i <= 10; i++) {
			// check if the skeleton is being tracked
			if (context.isTrackingSkeleton(i)) {
				drawSkeleton(i);
				if (System.currentTimeMillis() - time >= 5000) {
					System.out.println("in!!!");
					drawLastSkeleton(i);  // TODO just draw skeleton here!
					this.time = System.currentTimeMillis();
				}
			}
		}
		
	}
	
	private void drawLastSkeleton(int userId) {
		System.out.println("In Drawing last skeleton -- ");
		
		PVector torso = getJoint(userId, SimpleOpenNI.SKEL_TORSO);
		
		PVector neck = this.skeleton.getProportionedNeck(context, userId);
		PVector head = this.skeleton.getProportionedHead(context, userId);
		
		PVector leftShoulder = this.skeleton.getProportionedLeftShoulder(context, userId);
		PVector leftElbow = this.skeleton.getProportionedLeftElbow(context, userId);
		PVector leftHand = this.skeleton.getProportionedLeftHand(context, userId);
		PVector leftHip = this.skeleton.getProportionedLeftHip(context, userId);
		PVector leftKnee = this.skeleton.getProportionedLeftKnee(context, userId);
		PVector leftFoot = this.skeleton.getProportionedLeftFoot(context, userId);

		PVector rightShoulder = this.skeleton.getProportionedRightShoulder(context, userId);
		PVector rightElbow = this.skeleton.getProportionedRightElbow(context, userId);
		PVector rightHand = this.skeleton.getProportionedRightHand(context, userId);
		PVector rightHip = this.skeleton.getProportionedRightHip(context, userId);
		PVector rightKnee = this.skeleton.getProportionedRightKnee(context, userId);
		PVector rightFoot = this.skeleton.getProportionedRightFoot(context, userId);
		
		stroke(204, 102, 0);
		strokeWeight(5);
		drawLimb(torso, neck);
		drawLimb(neck, head);
		
		drawLimb(torso, leftShoulder);
		drawLimb(leftShoulder, leftElbow);
		drawLimb(leftElbow, leftHand);
		drawLimb(torso, leftHip);
		drawLimb(leftHip, leftKnee);
		drawLimb(leftKnee, leftFoot);

		drawLimb(torso, rightShoulder);
		drawLimb(rightShoulder, rightElbow);
		drawLimb(rightElbow, rightHand);
		drawLimb(torso, rightHip);
		drawLimb(rightHip, rightKnee);
		drawLimb(rightKnee, rightFoot);
	}
	
	private void drawLimb(PVector p1, PVector p2) {
		if (p1 != null && p2 != null) {
			line(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
	private PVector getJoint(int userId, int jointID) {
		PVector joint = new PVector();
		float confidence = context.getJointPositionSkeleton(userId, jointID, joint);
		if(confidence < 0.5){ 
			return null;
		}
		
		PVector convertedJoint = new PVector();
	    context.convertRealWorldToProjective(joint, convertedJoint);
	    ellipse(convertedJoint.x, convertedJoint.y, 5, 5);
	    return convertedJoint;
	}

	private void printPVector(PVector vector) {
		System.out.println("Vector position: " + vector.mag() + ", " + vector.heading());
	}

	public void updateSkelPos(int userId) {
		// get 3D position of a joint
		PVector positionReelPartieDuCorps = new PVector();
		PVector positionPartieDuCorps = new PVector();

		context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_HEAD,
				positionReelPartieDuCorps);
		
		context.convertRealWorldToProjective(positionReelPartieDuCorps,
				positionPartieDuCorps);
		
		System.out.println("Head: x=" + (context.depthWidth()
				- positionPartieDuCorps.x) + " y=" + positionPartieDuCorps.y
				+ " z=" + positionPartieDuCorps.z);
	}

	// draw the skeleton with the selected joints
	public void drawSkeleton(int userId) {
		stroke(0, 204, 102);
		strokeWeight(4);
		
		// draw limbs
		context.drawLimb(userId, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_LEFT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_LEFT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_ELBOW, SimpleOpenNI.SKEL_LEFT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_RIGHT_ELBOW);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_ELBOW, SimpleOpenNI.SKEL_RIGHT_HAND);

		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_SHOULDER, SimpleOpenNI.SKEL_TORSO);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER, SimpleOpenNI.SKEL_TORSO);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_LEFT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_HIP, SimpleOpenNI.SKEL_LEFT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_LEFT_KNEE, SimpleOpenNI.SKEL_LEFT_FOOT);

		context.drawLimb(userId, SimpleOpenNI.SKEL_TORSO, SimpleOpenNI.SKEL_RIGHT_HIP);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_HIP, SimpleOpenNI.SKEL_RIGHT_KNEE);
		context.drawLimb(userId, SimpleOpenNI.SKEL_RIGHT_KNEE, SimpleOpenNI.SKEL_RIGHT_FOOT); 
		

	}

	public void onNewUser(int userId) {
		System.out.println("New User Detected - userId: " + userId);

		// start pose detection
		context.startPoseDetection("Psi", userId);
	}

	public void onLostUser(int userId) {
		System.out.println("User Lost - userId: " + userId);
	}

	public void onStartPose(String pose, int userId) {
		System.out.println("Start of Pose Detected  - userId: " + userId
				+ ", pose: " + pose);

		// stop pose detection
		context.stopPoseDetection(userId);

		// start attempting to calibrate the skeleton
		context.requestCalibrationSkeleton(userId, true);
	}

	// when calibration begins
	public void onStartCalibration(int userId) {
		System.out.println("Beginning Calibration - userId: " + userId);
	}

	// when calibaration ends - successfully or unsucessfully
	public void onEndCalibration(int userId, boolean successfull) {
		System.out.println("Calibration of userId: " + userId
				+ ", successfull: " + successfull);

		if (successfull) {
			System.out.println("  User calibrated !!!");
			// begin skeleton tracking
			context.startTrackingSkeleton(userId);
		} else {
			System.out.println("  Failed to calibrate user !!!");

			// Start pose detection
			context.startPoseDetection("Psi", userId);
		}
	}
}
