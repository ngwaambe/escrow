#!groovy

node {
    checkout scm
}

pipeline {
    agent any

    tools {
        jdk 'openjdk-11'
        maven 'M3' // latest
    }

    stages {
        stage('Checkout') {
           steps {
              git 'https://github.com/ngwaambe/escrow.git'
           }
        }
        stage("Build and package") {
            steps {
                sh 'mvn clean verify'
            }
        }
    }
}
