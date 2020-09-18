ArrayList<String> getACMReportsIncrementedVersion(versionBuildNumber) {
    println "Current Version: ${versionBuildNumber}"

    final acmVersionStructure = new VersionStructure(
            versionPartsNames: [ "Major", "Minor", "Build", "Patch" ], 
            numberSeparatorToken: '.',
            versionSchemeRegex: "\\d+((\\.\\d+){0,3})?"
        )

    if (!acmVersionStructure.getIsVersionBuildNumberValid(versionBuildNumber)) {
        println "The build version was not incremented due to an unsupported version scheme."
        print "The currently supported version scheme is ${acmVersionStructure.versionFormat}"
        return versionBuildNumber
    }

    return acmVersionStructure.getIncrementedVersionScenarios(versionBuildNumber)
}

return this