import groovy.transform.Field

@Field final String numberSeparatorToken = '.'
@Field final ArrayList<String> versionPartsNames = [ "Major", "Minor", "Build", "Patch" ]
@Field final int minimumTotalVersionNumbers = 1
@Field final int maximumTotalVersionNumbers = versionPartsNames.size()
@Field final int totalSubVersionNumbers = versionPartsNames.size() - 1
@Field final GString versionSchemeRegex = /\d+(($numberSeparatorToken\d+){0,$totalSubVersionNumbers})?/

String getVersionFormat() { versionPartsNames.join(numberSeparatorToken) }

boolean getIsVersionBuildNumberValid(String versionBuildNumber) { versionBuildNumber ==~ versionSchemeRegex }

ArrayList<String> getIncrementedVersionScenarios(String versionBuildNumber) {
    return versionPartsNames.collect { getIncrementedVersionNumber(versionBuildNumber, versionPartsNames.indexOf(it)) }
}

String getIncrementedVersionNumber(String versionBuildNumber, int subVersionPosition = totalSubVersionNumbers) {
    final totalVersionNumbers = versionPartsNames.size()

    def getIncrementVersionExceptionMessage = {mode -> L:{
        def exceptionMessage = "Illegal version number ${mode} of ${versionBuildNumber} "
        exceptionMessage += "for position ${subVersionPosition}: ${versionPartsNames[subVersionPosition]}. "
        exceptionMessage += "Must be compatible with following version format: ${versionFormat}"
    }}

    if (getIsVersionBuildNumberValid(versionBuildNumber) == false || totalVersionNumbers < minimumTotalVersionNumbers || 
        totalVersionNumbers > maximumTotalVersionNumbers) {
        throw new IllegalArgumentException(getIncrementVersionExceptionMessage('input'))
    }

    final subVersionNumbers = versionBuildNumber.tokenize(numberSeparatorToken)
    final subVersionNumberToIncrement = subVersionNumbers[subVersionPosition] as int
    
    subVersionNumbers[subVersionPosition] = subVersionNumberToIncrement + 1
    def index = 0
    final incrementedSubVersionNumbers = subVersionNumbers.collect { index++ > subVersionPosition ? '0' : it }

    final incrementedVersionNumber = incrementedSubVersionNumbers.join(numberSeparatorToken)

    if (getIsVersionBuildNumberValid(versionBuildNumber) == false) {
        throw new UnsupportedOperationException(getIncrementVersionExceptionMessage('output'))
    }

    return incrementedVersionNumber
}

return this