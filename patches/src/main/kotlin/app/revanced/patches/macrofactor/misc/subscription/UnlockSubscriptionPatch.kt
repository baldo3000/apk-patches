package app.revanced.patches.macrofactor.misc.subscription
import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.removeInstructions
import app.revanced.patcher.extensions.InstructionExtensions.instructions
import app.revanced.patcher.patch.bytecodePatch

@Suppress("unused")
val unlockSubscriptionPatch = bytecodePatch(
    name = "Unlock subscription",
    description = "Unlocks the app.",
) {
    compatibleWith("com.sbs.train")

    execute {
        spoofAndroidCertFingerprint.method.removeInstructions(26)
        spoofAndroidCertFingerprint.method.addInstructions(
            0,
            """
                const/16 v0, 0x14
                new-array v0, v0, [B
                fill-array-data v0, :array_8
                return-object v0
            
                :array_8
                .array-data 1
                    0x44t
                    0x40t
                    -0x31t
                    -0x5t
                    -0x26t
                    -0x7ct
                    -0x74t
                    0x6ct
                    -0x75t
                    -0x62t
                    0x2t
                    -0x7ct
                    0x79t
                    0x50t
                    0x74t
                    0x58t
                    0x79t
                    -0x63t
                    0x51t
                    0x2ct
                .end array-data
            """
        )

        customerInfoFactoryBuildCustomerInfoFingerprint.method.addInstructions(
            0,
            """
                    const-string v3, "subscriber"
                    move-object/from16 v0, p1
                    invoke-virtual {v0, v3}, Lorg/json/JSONObject;->getJSONObject(Ljava/lang/String;)Lorg/json/JSONObject;
                    move-result-object v0
                    
                    const-string v4, "original_app_user_id"
                    invoke-virtual {v0, v4}, Lorg/json/JSONObject;->optString(Ljava/lang/String;)Ljava/lang/String;
                    move-result-object v2
                
                    const-string v1, "{\"request_date_ms\":1689736676913,\"request_date\":\"2023-07-19T03:17:56Z\",\"subscriber\":{\"non_subscriptions\":{},\"first_seen\":\"2023-07-19T03:17:14Z\",\"original_application_version\":\"39\",\"other_purchases\":{},\"management_url\":\"https://apps.apple.com/account/subscriptions\",\"subscriptions\":{\"com.sbs.train.subscription.1\":{\"original_purchase_date\":\"2023-07-19T03:17:53Z\",\"expires_date\":\"2099-08-02T03:17:52Z\",\"is_sandbox\":false,\"refunded_at\":null,\"unsubscribe_detected_at\":null,\"grace_period_expires_date\":null,\"period_type\":\"normal\",\"purchase_date\":\"2023-07-19T03:17:52Z\",\"billing_issues_detected_at\":null,\"ownership_type\":\"PURCHASED\",\"store\":\"app_store\",\"auto_resume_date\":null}},\"entitlements\":{\"subscription_workouts\":{\"grace_period_expires_date\":null,\"purchase_date\":\"2023-07-19T03:17:52Z\",\"product_identifier\":\"com.sbs.train.subscription.1\",\"expires_date\":\"2099-08-02T03:17:52Z\"}},\"original_purchase_date\":\"2023-07-18T21:29:29Z\",\"original_app_user_id\":\"id\",\"last_seen\":\"2023-07-19T03:17:14Z\"}}"
                    new-instance v0, Lorg/json/JSONObject;
                    invoke-direct {v0, v1}, Lorg/json/JSONObject;-><init>(Ljava/lang/String;)V
                    
                    invoke-virtual {v0, v3}, Lorg/json/JSONObject;->getJSONObject(Ljava/lang/String;)Lorg/json/JSONObject;
                    move-result-object v1
                    
                    invoke-virtual {v1, v4, v2}, Lorg/json/JSONObject;->put(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
                    move-object/from16 p1, v0
            """
        )

        val spoofInstallerSmali = """
            const-string v0, "com.android.vending"
            return-object v0
        """

        // Use replaceInstructions at index 0 to overwrite the start of the method
        // This is safer than removing the whole method body if there are exception handlers
        fun safeSpoof(method: app.revanced.patcher.util.proxy.mutableTypes.MutableMethod) {
            method.apply {
                // We inject at the very top and immediately return.
                // This makes the rest of the method unreachable, satisfying the verifier.
                addInstructions(0, spoofInstallerSmali)
            }
        }

        safeSpoof(firebaseInstallerFingerprint.method)
        safeSpoof(firebaseIdManagerFingerprint.method)
        safeSpoof(flutterPackageInfoFingerprint.method)
    }
}
