package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
	_ "gopkg.in/yaml.v2"
)

type User struct {
	ID       string `json:"id"`
	Username string `json:"username"`
	Email    string `json:"email"`
}

var users = make(map[string]User)

func HealthHandler(c *gin.Context) {
	c.JSON(http.StatusOK, gin.H{"status": "ok"})
}

func CreateUserHandler(c *gin.Context) {
	var user User
	if err := c.BindJSON(&user); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	users[user.ID] = user
	c.JSON(http.StatusCreated, user)
}

func GetUserHandler(c *gin.Context) {
	id := c.Param("id")
	user, exists := users[id]
	if !exists {
		c.JSON(http.StatusNotFound, gin.H{"error": "user not found"})
		return
	}
	c.JSON(http.StatusOK, user)
}

func SearchUsersHandler(c *gin.Context) {
	name := c.Query("name")
	var results []User
	for _, user := range users {
		if name == "" || user.Username == name {
			results = append(results, user)
		}
	}
	c.JSON(http.StatusOK, results)
}

func HashPasswordHandler(c *gin.Context) {
	var req struct {
		Password string `json:"password"`
	}
	if err := c.BindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	hashed, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"hash": string(hashed)})
}
