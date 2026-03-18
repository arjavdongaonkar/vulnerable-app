package main

import (
	"log"

	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()

	// Public routes
	r.GET("/health", HealthHandler)
	r.POST("/login", LoginHandler)
	r.POST("/users", CreateUserHandler)
	r.GET("/users/:id", GetUserHandler)
	r.GET("/users", SearchUsersHandler)
	r.POST("/hash", HashPasswordHandler)

	// Protected routes
	protected := r.Group("/api")
	protected.Use(AuthMiddleware())
	{
		protected.GET("/profile", ProtectedHandler)
	}

	log.Println("Starting server on :8080")
	if err := r.Run(":8080"); err != nil {
		log.Fatal(err)
	}
}
