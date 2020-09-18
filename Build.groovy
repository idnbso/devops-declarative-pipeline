import com.cwctravel.hudson.plugins.extended_choice_parameter.ExtendedChoiceParameterDefinition

pipeline {
    agent any
    stages {
        stage('Setup Pipeline') {
            steps {
                script {
                    def acmVersionStructure = load "scripts/ACMVersionStructure.groovy"

                    // Read local stored version json file and deserialize to an object
                    final jsonFileName = 'version.json'
                    final versionFileText = readFile(jsonFileName)
                    final versionFile = new groovy.json.JsonSlurperClassic().parseText(versionFileText)
                    final versionBuildNumber = versionFile.build_version

                    def (majorRelease, minorRelease, buildRelease, patchRelease) = acmVersionStructure.getIncrementedVersionScenarios(versionBuildNumber)
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