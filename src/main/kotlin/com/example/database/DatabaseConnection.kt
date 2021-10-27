package com.example.database

import me.liuwj.ktorm.database.Database

object DatabaseConnection {
    val db = Database.connect(
            url = "jdbc:mysql://localhost:3306/ktor_db",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = "password;"
        )
}