Map modules = [:]

pipeline {
    agent any
    stages {
        stage('Setup Pipeline') {
            steps {
                script {
                    modules.utils = load "utils.groovy"

                    // Read local stored version json file and deserialize to an object
                    final jsonFileName = 'version.json'
                    final versionFileText = readFile(jsonFileName)
                    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)
                    final versionBuildNumber = versionFile.build_version

                    def (majorRelease, minorRelease, buildRelease, patchRelease) = modules.utils.getACMReportsIncrementedVersion(versionBuildNumber)
                    println "Incremented Version Variables: Major: ${majorRelease}, Minor: ${minorRelease}, Build: ${buildRelease}, Patch: ${patchRelease}"

                    properties([parameters([new ExtendedChoiceParameterDefinition(
                                "Release", // name
                                "PT_RADIO", // type
                                "${majorRelease},${minorRelease},${buildRelease},${patchRelease}",  // propertyValue
                                "", // projectName
                                "", // propertyFile
                                "", // groovyScript
                                "", // groovyScriptFile
                                "", // bindings
                                "", // groovyClasspath
                                "", // propertyKey
                                "${patchRelease}", // defaultPropertyValue
                                "", // defaultPropertyFile
                                "", // defaultGroovyScript
                                "", // defaultGroovyScriptFile
                                "", // defaultBindings
                                "", // defaultGroovyClasspath
                                "", // defaultPropertyKey
                                "Major: ${majorRelease},Minor: ${minorRelease},Build: ${buildRelease},Patch: ${patchRelease}", // descriptionPropertyValue
                                "", // descriptionPropertyFile
                                "", // descriptionGroovyScript
                                "", // descriptionGroovyScriptFile
                                "", // descriptionBindings
                                "", // descriptionGroovyClasspath
                                "", // descriptionPropertyKey
                                "", // javascriptFile
                                "", // javascript
                                false, // saveJSONParameterToFile
                                false, // quoteValue
                                4, // visibleItemCount
                                "Choose the next release version (last version was ${versionBuildNumber})", // description
                                "," // multiSelectDelimiter
                            )])])
                        
                    echo "Selected ${params.Release}"
                }
            }
        }
    }
}