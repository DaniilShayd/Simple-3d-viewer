package com.cgvsu.math;

import com.cgvsu.constants.ProjectContstants;

// Это заготовка для собственной библиотеки для работы с линейной алгеброй
public class Vector3f {
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean equals(Vector3f other) {
        // todo: желательно, чтобы это была глобальная константа (Сделано)
        return Math.abs(x - other.x) < ProjectContstants.getEps() && Math.abs(y - other.y)
                < ProjectContstants.getEps() && Math.abs(z - other.z) < ProjectContstants.getEps();
    }

    public float x, y, z;
}
