.PHONY: compile package run run-jar clean all help git-init test

ifneq (,$(wildcard ./.env))
include .env
export
endif

REPO_NAME := $(shell basename $(CURDIR))
PROJECT := $(CURDIR)
LOCAL_BIN := $(CURDIR)/bin

# Maven
all: test compile run

compile:
	mvn compile

package:
	mvn package

run:
	java -cp target/classes com.example.Main

run-jar:
	java -jar target/HelloWorld-1.0.0.jar

test:
	mvn test

clean:
	mvn clean

help:
	@echo "Available commands:"
	@echo "  make all     - Compiles and runs the application"
	@echo "  make compile - Compiles the source code"
	@echo "  make package - Compiles the source code to .jar file"
	@echo "  make run     - Runs the application"
	@echo "  make run-jar - Runs the application from .jar file"
	@echo "  make test    - Runs all tests"
	@echo "  make clean   - Cleans the project"

# Git
git-init:
	#gh repo create $(GIT_USER)/$(REPO_NAME) --public
	git init
	git config user.name "$(GIT_USER)"
	git config user.email "$(GIT_EMAIL)"
	git add .gitignore
	git commit -m "Init commit"
	git remote add origin git@github.com:$(GIT_USER)/$(REPO_NAME).git
	git remote -v
	git push -u origin master