package app.revanced.patches.macrofactor.misc.pairip

import app.revanced.patcher.fingerprint

internal val processLicenseResponseFingerprint = fingerprint {
    custom { method, classDef ->
        classDef.type == "Lcom/pairip/licensecheck/LicenseClient;" &&
                method.name == "processResponse"
    }
}

internal val validateLicenseResponseFingerprint = fingerprint {
    custom { method, classDef ->
        classDef.type == "Lcom/pairip/licensecheck/ResponseValidator;" &&
                method.name == "validateResponse"
    }
}
