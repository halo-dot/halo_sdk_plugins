package za.co.synthesis.halo.sdk_ui.example

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import za.co.synthesis.halo.sdk_ui.HaloSdkUi
import kotlinx.coroutines.launch
import za.co.synthesis.halo.sdk_ui.models.HDMerchantDetails
import za.co.synthesis.halo.sdk_ui.models.HDCompanyLogo
import za.co.synthesis.halo.sdk_ui.models.HDConfig
import za.co.synthesis.halo.sdk_ui.models.HaloColorScheme
import za.co.synthesis.halo.sdk_ui.models.HaloTheme
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Duration
import java.time.Instant
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HaloSdkUi.init(
            HDConfig(
                this@MainActivity,
                merchantDetails = HDMerchantDetails(
                    "za.co.synthesis.example",
                    "0.0.1",
                    "Tom's Bike Shop",
                ),
                onTokenRequest = ::getJWTOffline,
//                theme = HaloTheme(
//                    logo = HDCompanyLogo(
//                        "pxp-logo-dark.png",
//                        "pxp-logo-light.svg",
//                        aspectRatio = 2f,
//                    ),
//                    light = HaloColorScheme.default().copy(
//                        primary = Color(0xFF292CF5),
//                        secondary = Color(0xFF292CF5),
//                        outline = Color(0x80666666),
//                    ),
//                    dark = HaloColorScheme.defaultDark().copy(
//                        primary = Color(0xFF292CF5),
//                        secondary = Color(0xFF292CF5),
//                        outline = Color(0xFF999999),
//                    ),
//                    shape = 20.dp
//                )
            )
        )

        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        lifecycleScope.launch {
                            val result = HaloSdkUi.launch(
                                null,
//                                BigDecimal.valueOf(100.0),
//                                "Ref passed from the host",
                                null,
                                null
                            )
                            Log.d("MainActivity", "Transaction result: $result")
                        }
                    }) {
                    Text("Launch SDK")
                }
            }
        }
    }
}

// Parsed once — key parsing + signer setup is expensive and onRequestJWT is
// called repeatedly during a transaction.
private val jwtSigner: RSASSASigner by lazy {
    val privateKey = KeyFactory.getInstance("RSA")
        .generatePrivate(PKCS8EncodedKeySpec(Base64.decode(privateKeyPem, Base64.DEFAULT)))
    RSASSASigner(privateKey)
}

fun getJWTOffline(): String {
    Log.d("HaloTest", "Getting JWT offline")

    val claims = JWTClaimsSet.Builder()
        .subject("{D8208288-E869-4726-B198-364D66EC9243}")
        .issuer("https://portal.iveri.net/")
        .audience("kernelserver.qa.haloplus.io")
        .claim("aud_fingerprints", "sha256/CNOtjib4NAlSqDZDY5aknDcVbcfLEWBgnGl/dgec4aA=")
        .claim("usr", "bob")
        .issueTime(Date())
        .expirationTime(
            Date.from(Instant.now().plus(Duration.ofMinutes(15)))
        )

    val signedJwt = SignedJWT(
        JWSHeader.Builder(JWSAlgorithm.RS512).build(),
        claims.build()
    )

    signedJwt.sign(jwtSigner)

    return signedJwt.serialize()
}

private val privateKeyPem =
        "" +
                "MIIJJwIBAAKCAgEAvP164Pcxo9tVM5dNVmFXVqEhF4yONuETwrlBvE3C42ZxGH23\n" +
                "jLCEiRhTGl6rSy/5KHnmt7dG1YvyQif9UldW1uEGE0e5GcJyitc6tCIxoiRgKnpj\n" +
                "BRfaoJfSjIKSuBm56XRQhdHUmRYMT8lRH9F2zjNXH4vNxafDUPbfw5+SKZShDJfI\n" +
                "ZAZzOJ7+BzAXtDHQ6J/JZ863ERiqC5W2fqBFlfn4Gr1JGZaMnrXQP503IU+zOM8j\n" +
                "rqcnFHblss+YRBsQxhF3Pg81Ix85r5wASKCbWx8X+fYnxu42SFCSe9BdvshCwI4V\n" +
                "bihPXwd4g+ODwrKEGMxTLpbEJVAjrEfWb2y5iNwUHHAZO9QS9Y75+z4e9CuYOdGQ\n" +
                "YBALAtH3ZACgquXlqKlamaLIr4boLWlExxVTX9iz3zrJnhXPFvTkSUwVkPiJJkAj\n" +
                "LM7KjjhHsrYR4zmH8xvqz3bwLLMACxD2ZfUDJnzdZFE2ZSrp09vi4et+wbs3+yfI\n" +
                "fpMAmBvnuywEfPIv/UaFbosSAnx3TZUh/kOUjgu9/KtVy/ZzcNneGv0U35ibuqsy\n" +
                "2Pgkze0byUO26dZPtC0BWKlYHokDG4/4WGW3ijrNqY5MdzcKWU+5REwUJ18lRatR\n" +
                "MPKPzrjKVoxeWXZL1LTWw7fTj1ElTOcwInGfRNgIgYVEWrnJ/D8CIVEn1E0CAwEA\n" +
                "AQKCAgAVc9PRb9h/fY5SgLUOKOwq5g0QE8ExjeNaLKuUuOJLlsggBrvhk/M6GVFB\n" +
                "l90WIqLD4JvQXypYDikt2kU2kLnHmBvCRNdy4EVz6NIokA3Alnl2COtu22d9PY6b\n" +
                "AC1bCXc20/WvTa4LbIXG6WwJ0miL0nxsFR4CoYmZtKmaFpBsTjXpFzJE1w3thPAC\n" +
                "l90bnrX/LzgY+Pgt0CC+l5Fza9+fkmmCe4RYi7l4QIdxg802Ekpz7eC32rskZwn+\n" +
                "00gOSbY3OCn9bj4oDPYq2WiXgJJHSc8abWx+zRvLTJ4Ra4m19hx2/V8wYGdWEE6H\n" +
                "8Lj2+PWRV7baKHypBvZDAD/hPl8MQxGIYKeaOByIpOZ7tqqitMawWtW7qtxN4vE+\n" +
                "4Ajdehb9t9a5BIApGTiF7+0MtsjWbLjDAmYV5tSl3jCpCkZ16Z01zD9EM/Oj/h0v\n" +
                "YB5J69K1pHk6ZJoNuPgS73jl0agw7HIOhaBnsxKXK3NOu1Bd1Q+3f7MMc5/3q459\n" +
                "Y8B6YPOapOY875rGh4LfId+zPVnkM81WFTJapu+FtZUGoe5b9PagstsaJd+oiJob\n" +
                "dvM61WfZhHbDIlJseT0xHY59SkbZ7ljMdMngm3LhN5UCzfG/LTPUHQieAfwOXiIG\n" +
                "LeX6AtpJGzkDHk8nKVOIN9UmzRwJb3lnA7EZPdQR5+oYc5eQfwKCAQEAy2MF8vAr\n" +
                "dxs+99aYVc0cmMMeXl5e17xDTMuiFHPkDit8+JMwSpRuypcgKIbjML4X/MUIosv5\n" +
                "WQ1yDRXMfagIPCenMYYrlGSgxlZSDGU5bobhQ0xg2hkufptpGXNAYdnccbNT5Ien\n" +
                "zHhlpZPDsJBVm4+xgSQUL5Pd+rpx2XA/jRKgwuac0AmQddMhuZ0yES4+GUhgsfi7\n" +
                "cup2mZHW0C7RoiPLABqJ+ZbfpqJIE05cvTfna1sP9UYvaLHDc/RWk8ICrCtTHNaS\n" +
                "qCN6bMYVzM0wCqtkYCZChytXq28XrxWNlaPJF4F/9Zrhn/1Z96Xei/D3KzOfyuYN\n" +
                "4n0qz/04RFJT3wKCAQEA7eEPh/jrZB3hG/dG8q1jq3JbLlgt+Fpgc/o0QQiTDbFb\n" +
                "MPqWeAMmDA67yHzlwEsdWhmNqZbpd7Y8Pt5/pFGHAz5Ln5c8l/QsXvhCrGG0sTC5\n" +
                "9kCg2WbfLOGHvjLHJqKxkCP1AjrrSwk0PLpOfschOMD7d/6ZhZCNP8rxdZHSGuei\n" +
                "bzTVpcapUR/uR4ii70Auc45xG0MQu7aQNhkWbO3V6q3oFLmg2F/uI+EojVaZ3bmk\n" +
                "7jFNHGyA1CbMVa58Z8ZMCLRUqA/FjYaaEZOiNHIQRmguYdxKrQDaD9CdnyuVVX5j\n" +
                "9+EveorZx+Pp/MU7asDxI7/YO49wDZ7yY85wptW9UwKCAQBylInQcF98TVwvEbVm\n" +
                "eRRIN3xPW8kAru4XQI1CCnwLQ5K3E2zcRz0XYwZZ7ovldOJco8AWvuTtt3ZuSr6W\n" +
                "d+iTwx8c9mJsyjgNJKo6k3hEfTZAzLGvjQo1h2YxUU2PEsZvQ8DaeVYeCiHxowpW\n" +
                "2SlxZwVwJJw+pYYj74+eGO+kkT8CLhtf1FOPHW/deuKu7SdM5QqcmO05cz93isLD\n" +
                "K+0r9BA0Mb9TXh0HaltCjR4P94OnZnjaDD0/NQ5Qr/Fqcp1A03OSRov+r2YUC5BS\n" +
                "fbI4AJCe3k8SiWdtjDuChpjqGYh7dphZDHVZxcg+jTKQEiOdMgsuqmZeF3fcDuL5\n" +
                "EoxvAoIBAB0dH35/y6d8NLHsiMVozTsNivVYG4A653UqWTzgZMcXPtb2xHW5xO1S\n" +
                "3E7EYoYva088s2bfLoHVTvhO2QZuZ/zQogbNRsA+RTP2ZIqehrPtB8WI0WbqPURL\n" +
                "8gYoRtGGtf6zN+hm96c+YCNSlgrUKk+85sof4gBD2dXF69l82F8snGfJ6qAYeV6R\n" +
                "buNYaMCpdUX1WAiDI8glpu7IEUORtE29rRrkQThaPVYs136866YEc5gndo2VBgtx\n" +
                "GC8gZN96mJG0KGxNSdtxbufZzs9manGDjLGzeXxFD2tCGBiECAnnpyqOZ/Yyikb5\n" +
                "sQibxxLgvGKotzlK6+bu3jGy8XypyjsCggEAclkv+j2+KKoXRDQpB0KvIvtnvIWe\n" +
                "jAKPRW5RAReKE9middMHDbegX+af2C8lB4IDZCsD3ZEoyRTpDEHXRqrh0KNHrZs/\n" +
                "dKuGOdumO/Be5QYz3kxWOXilMR4BQKZgCW2La5faXe3KOb3zkjEcUGay0HaQ6igY\n" +
                "SBmlP3h+EWkaWg4Jfd3++5DHqmOuoCsIYoFZLdWvswhQ+/fTCNfKPN2hUtYgjnAN\n" +
                "l9vBNFBxYn8ZYlzP6rerIJ1/+BceI5KsvVL+Z9aaX0yX3MYiDDOm50aUkyZs+008\n" +
                "mrUFFb02JkA1bMnd71Urd29bDt5rLdxibuUcECxX9Q3u+LIjtx/0UEN89A==\n" +
                ""