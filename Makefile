.PHONY: compile package run run-jar clean all help git-init

ifneq (,$(wildcard ./.env))
include .env
export
endif

REPO_NAME := $(shell basename $(CURDIR))
PROJECT := $(CURDIR)
LOCAL_BIN := $(CURDIR)/bin

# Maven
all: compile run

compile:
	mvn compile

package:
	mvn package

run:
	java -cp target/classes Main

run-jar:
	java -jar target/HelloWorld-1.0.0.jar

clean:
	mvn clean

help:
	@echo "Available commands:"
	@echo "  make compile - Compiles the source code"
	@echo "  make run     - Runs the application"
	@echo "  make run-jar - Runs the application from .jar file"
	@echo "  make clean   - Cleans the project"
	@echo "  make all     - Compiles and runs the application"

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