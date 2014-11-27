package testkinect;

import java.util.HashMap;

import processing.core.PVector;
import SimpleOpenNI.IntVector;
import SimpleOpenNI.SimpleOpenNI;

public class SkeletonOrientations {
	private HashMap<Integer, PVector> directions;
	
	public SkeletonOrientations() {
		this.directions = new HashMap<Integer, PVector>();
	}
	
	public void saveSekeleton(SimpleOpenNI context) {
		if (this.directions == null) {
			this.directions = new HashMap<Integer, PVector>();
		}
		
		IntVector userList = new IntVector();
		context.getUsers(userList);
		if (userList.size() > 0) {
			int userId = userList.get(0);
			// Construct orientations of projected skeleton orientations
			if (context.isTrackingSkeleton(userId)) {
				PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
				
				PVector left_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_SHOULDER);
				PVector difference = PVector.sub(left_shoulder, torso);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_SHOULDER, difference);
				
				PVector left_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_ELBOW);
				difference = PVector.sub(left_elbow, left_shoulder);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_ELBOW, difference);
				
				PVector left_hand = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_HAND);
				difference = PVector.sub(left_hand, left_elbow);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_HAND, difference);
				
				PVector neck = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_NECK);
				difference = PVector.sub(neck, torso);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_NECK, difference);
				
				PVector head = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_HEAD);
				difference = PVector.sub(head, neck);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_HEAD, difference);
				
				PVector right_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
				difference = PVector.sub(right_shoulder, torso);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_SHOULDER, difference);
				
				PVector right_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_ELBOW);
				difference = PVector.sub(right_elbow, right_shoulder);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_ELBOW, difference);
				
				PVector right_hand = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_HAND);
				difference = PVector.sub(right_hand, right_elbow);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_HAND, difference);
				
				PVector left_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_HIP);
				difference = PVector.sub(left_hip, torso);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_HIP, difference);
				
				PVector left_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_KNEE);
				difference = PVector.sub(left_knee, left_hip);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_KNEE, difference);
				
				PVector left_foot = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_FOOT);
				difference = PVector.sub(left_foot, left_knee);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_LEFT_FOOT, difference);
				
				PVector right_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_HIP);
				difference = PVector.sub(right_hip, torso);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_HIP, difference);
				
				PVector right_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_KNEE);
				difference = PVector.sub(right_knee, right_hip);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_KNEE, difference);
				
				PVector right_foot = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_FOOT);
				difference = PVector.sub(right_foot, right_knee);
				difference.normalize();
				directions.put(SimpleOpenNI.SKEL_RIGHT_FOOT, difference);
				
				System.out.println("Current skeleton saved!");
			}
		}
	}
	
	private PVector getProjectedPVector(SimpleOpenNI context, int userId, int skelNumber) {
		PVector vector = new PVector();
		PVector projectedVector = new PVector();
		context.getJointPositionSkeleton(userId, skelNumber, vector);
		context.convertRealWorldToProjective(vector, projectedVector);
		return projectedVector;
	}
	
	public PVector getProportionedNeck(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_NECK)) {
			PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
			PVector neck = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_NECK);
			PVector difference = PVector.sub(neck, torso);
			
			return PVector.add(torso, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_NECK), difference.mag()));
		}
		else {
			return null;
		}
	}
	
	public PVector getProportionedHead(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_HEAD)) {
			PVector neck = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_NECK);
			PVector head = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_HEAD);
			PVector difference = PVector.sub(head, neck);
			
			return PVector.add(neck, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_HEAD), difference.mag()));
		}
		else {
			return null;
		}
	}
	
	public PVector getProportionedLeftElbow(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_ELBOW)) {
			PVector left_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_SHOULDER);
			PVector left_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_ELBOW);
			PVector difference = PVector.sub(left_elbow, left_shoulder);
			
			return PVector.add(left_shoulder, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_ELBOW), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedLeftHand(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_HAND)) {
			PVector left_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_ELBOW);
			PVector left_hand = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_HAND);
			PVector difference = PVector.sub(left_hand, left_elbow);
			
			return PVector.add(left_elbow, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_HAND), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedLeftHip(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_HIP)) {
			PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
			PVector left_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_HIP);
			PVector difference = PVector.sub(left_hip, torso);
			
			return PVector.add(torso, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_HIP), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedLeftKnee(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_KNEE)) {
			PVector left_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_HIP);
			PVector left_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_KNEE);
			PVector difference = PVector.sub(left_knee, left_hip);
			
			return PVector.add(left_hip, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_KNEE), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedLeftFoot(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_FOOT)) {
			PVector left_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_KNEE);
			PVector left_foot = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_FOOT);
			PVector difference = PVector.sub(left_foot, left_knee);
			
			return PVector.add(left_knee, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_FOOT), difference.mag()));
		}
		else {
			return null;
		}
	}
	
	public PVector getProportionedLeftShoulder(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_LEFT_SHOULDER)) {
			PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
			PVector left_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_LEFT_SHOULDER);
			PVector difference = PVector.sub(left_shoulder, torso);
			
			return PVector.add(torso, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_LEFT_SHOULDER), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedRightShoulder(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_SHOULDER)) {
			PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
			PVector right_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
			PVector difference = PVector.sub(right_shoulder, torso);
			
			return PVector.add(torso, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_SHOULDER), difference.mag()));
		}
		else {
			return null;
		}
	}
	
	public PVector getProportionedRightElbow(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_ELBOW)) {
			PVector right_shoulder = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_SHOULDER);
			PVector right_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_ELBOW);
			PVector difference = PVector.sub(right_elbow, right_shoulder);
			
			return PVector.add(right_shoulder, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_ELBOW), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedRightHand(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_HAND)) {
			PVector right_elbow = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_ELBOW);
			PVector right_hand = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_HAND);
			PVector difference = PVector.sub(right_hand, right_elbow);
			
			return PVector.add(right_elbow, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_HAND), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedRightHip(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_HIP)) {
			PVector torso = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_TORSO);
			PVector right_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_HIP);
			PVector difference = PVector.sub(right_hip, torso);
			
			return PVector.add(torso, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_HIP), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedRightKnee(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_KNEE)) {
			PVector right_hip = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_HIP);
			PVector right_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_KNEE);
			PVector difference = PVector.sub(right_knee, right_hip);
			
			return PVector.add(right_hip, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_KNEE), difference.mag()));
		}
		else {
			return null;
		}
	}
	public PVector getProportionedRightFoot(SimpleOpenNI context, int userId) {
		if (context.isTrackingSkeleton(userId) && this.directions.containsKey(SimpleOpenNI.SKEL_RIGHT_FOOT)) {
			PVector right_knee = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_KNEE);
			PVector right_foot = getProjectedPVector(context, userId, SimpleOpenNI.SKEL_RIGHT_FOOT);
			PVector difference = PVector.sub(right_foot, right_knee);
			
			return PVector.add(right_knee, PVector.mult(this.directions.get(SimpleOpenNI.SKEL_RIGHT_FOOT), difference.mag()));
		}
		else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		PVector v1 = new PVector(1, 1);
		PVector v2 = new PVector(1, 0);
		
		PVector diff = PVector.sub(v1, v2);
		diff.normalize();
		v1.normalize();
		System.out.println("diff: " + diff);
		System.out.println("v1: " + v1);
		System.out.println("heading: " + diff.heading());
		System.out.println("mag: " + diff.mag());
	}
}
