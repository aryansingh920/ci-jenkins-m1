/* groovylint-disable CompileStatic, ImplicitClosureParameter, JavaIoPackageAccess, LineLength, NoDef, UnnecessaryGString, UnusedVariable, VariableTypeRequired */

// class Deploy {
//     static void main(String[] args) {
//         println "Compiling and running Groovy on Mac!"
//     }
// }

def app = 'frontend'
def env = 'prod'

// The standard way to build a command
def deployCmd = "docker deploy ${app} --env ${env}"

// Multi-line shell script block
def shellScript = """
    echo "Starting deployment..."
    docker pull my-repo/${app}:latest
    docker run -d ${app}
"""

def stages = ['Build', 'Test', 'Deploy']

stages << 'Notify'       // Add to list
println stages[0]        // Access index 0
println stages.join(', ') // "Build, Test, Deploy, Notify"

def config = [
    port: 8080,
    label: 'docker-agent',
    timeout: 10
]

// Both work:
println config.port
println config['label']

// Instead of: if(params != null && params.BRANCH != null)
def branch = config?.BRANCH

println branch

// If 'timeout' isn't defined, use 30.
def waitTime = config?.timeout ?: 30

println waitTime

// Typical Jenkins structure is just Groovy Closures
// node('worker-1') {
//     stage('Setup') {
//         echo "Doing stuff..."
//     }
// }

import groovy.json.JsonSlurper

// 1. Reading a file (Standard Groovy)
def contents = new File('config.json').text

// 2. Parsing JSON
def json = new JsonSlurper().parseText(contents)
println "Building version: ${json.label}"

// -----------

def pods = ['auth-pod', 'api-pod', 'db-pod']

// Find all pods starting with 'a'
def aPods = pods.findAll { it.startsWith('a') }

// Execute a command for each pod
aPods.each { println "kubectl logs ${it}" }


def deployments = [
    [name: 'web-app', replicas: 3],
    [name: 'api-service', replicas: 2],
    [name: 'database', replicas: 1]
]

deployments.each { dep ->
    if (dep.replicas > 1) {
        println "Scaling ${dep.name} to ${dep.replicas} instances..."
    } else {
        println "Starting singleton service: ${dep.name}"
    }
}
