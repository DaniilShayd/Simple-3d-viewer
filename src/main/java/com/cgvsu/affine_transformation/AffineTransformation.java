package com.cgvsu.affine_transformation;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;

import com.cgvsu.math.Vector3f;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

public class AffineTransformation {

    /*public static void translateModel(Model model, double x, double y, double z) {
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        translationMatrix.setTranslation(new Vector3d(x, y, z));
        applyTransformation(model, translationMatrix);
        calculateNormals(model);
    }*/
    public static void translateModel(Model model, double x, double y, double z) {
        for (Vector3f vertex : model.vertices) {
            vertex.x += x;
            vertex.y += y;
            vertex.z += z;
        }
    }
    public static void rotateModel(Model model, double angleX, double angleY, double angleZ) {
        Matrix4d rotationMatrixX = new Matrix4d();
        Matrix4d rotationMatrixY = new Matrix4d();
        Matrix4d rotationMatrixZ = new Matrix4d();

        rotationMatrixX.setIdentity();
        rotationMatrixX.rotX(angleX);

        rotationMatrixY.setIdentity();
        rotationMatrixY.rotY(angleY);

        rotationMatrixZ.setIdentity();
        rotationMatrixZ.rotZ(angleZ);

        Matrix4d combinedRotationMatrix = new Matrix4d();
        combinedRotationMatrix.mul(rotationMatrixX, rotationMatrixY);
        combinedRotationMatrix.mul(combinedRotationMatrix, rotationMatrixZ);

        applyTransformation(model, combinedRotationMatrix);
        calculateNormals(model);
    }

    public static void scaleModel(Model model, double scaleX, double scaleY, double scaleZ) {
        Matrix4d scalingMatrix = new Matrix4d();
        scalingMatrix.setIdentity();
        scalingMatrix.m00 = scaleX;
        scalingMatrix.m11 = scaleY;
        scalingMatrix.m22 = scaleZ;
        applyTransformation(model, scalingMatrix);
        calculateNormals(model);
    }

    private static void applyTransformation(Model model, Matrix4d transformationMatrix) {
        for (Vector3f vertex : model.vertices) {
            Vector3d transformedVertex = new Vector3d(vertex.x, vertex.y, vertex.z);
            transformationMatrix.transform(transformedVertex);
            vertex.x = (float) transformedVertex.x;
            vertex.y = (float) transformedVertex.y;
            vertex.z = (float) transformedVertex.z;
        }
    }
    private static void calculateNormals(Model model) {
        for (int i = 0; i < model.polygons.size(); i++) {
            Polygon polygon = model.polygons.get(i);
            Vector3f v1 = model.vertices.get(polygon.getVertexIndices().get(0));
            Vector3f v2 = model.vertices.get(polygon.getVertexIndices().get(1));
            Vector3f v3 = model.vertices.get(polygon.getVertexIndices().get(2));

            Vector3d edge1 = new Vector3d(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
            Vector3d edge2 = new Vector3d(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);

            Vector3d normal = new Vector3d();
            normal.cross(edge1, edge2);
            normal.normalize();

            model.normals.add(new Vector3f((float)normal.x, (float)normal.y, (float)normal.z));
        }
    }

}

