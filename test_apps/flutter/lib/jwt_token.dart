import 'package:dart_jsonwebtoken/dart_jsonwebtoken.dart';
import 'package:sdkflutterplugin_example/config.dart';

// if you provide a JWT token, it will be used instead of generating a new one
const String tempJwt =
    "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJ1c3IiOiJzdGV2ZW5Ac3ludGhlc2lzLmNvLnphIiwiYXVkX2ZpbmdlcnByaW50cyI6InNoYTI1Ni9VYTdiRVRPS0NvVGEzTFVTdE5RbHJINkxJd3VJdFpnZkpqemZZNzBKNWgwPSIsImtza19waW4iOiJzaGEyNTYvMVpuYTRUNlBLY0ozS3EvZGJWeWxiOG42MmovQWRRWVV6V3JqLzRzazVROD07c2hhMjU2LzVtQWZsVk1YREQvTXFpZGhLYjY2ZFlFRHBDWGFrZ2tLMU5Oc3pWVktGc0U9O3NoYTI1Ni96YkRIQlNlOWhxYUZFdjFmMTVnUHVoaEdBVmQwYVRzYy9CaCsxRCttUkkwPSIsImlhdCI6MTcyMjQ5OTI3NCwiZXhwIjoxNzIyNTAwMTc0LCJhdWQiOiJrZXJuZWxzZXJ2ZXIuemEuZGV2LmhhbG9wbHVzLmlvIiwiaXNzIjoiemEuY28uc3ludGhlc2lzLmhhbG8ubXBvcy5nbyIsInN1YiI6IjVkYTkwMzBmLWNlY2EtNGVmMS1hYTE2LWU3ZTY0MmU1MzQ3MSJ9.VkImQSV6xMaxK_2tm1hGmpkhTx3nmrbcnlWu7Q_PBsUCxg7LW8CVVpuwx24zy2T8SMtf4sMzz-xDCJNddEiqsv_M2a_L-yn9m86jnqoAyHVJmeaDlIa1_2uJf1kX-qFLbHUHJMOnfcAHUb3bpkZVjZ1BoqLKVI-RTvLvU4CtBWaiWDsR56HD7TcVmxPpFNjVjr_KW5ic0D6t1zagJHitkUvRKMxgSWDh6AqCZLjiPI3-r57ie-146aFLFqlY7QMruSvL8tfbQ-nWTE62IyiVsT1w39zp5WM4b2V4yT9yu4F00QeWXcN6aVsmkN8GlXxNWZGgz0hvNVAd3uYlWCWr1Q7p60T4Ldv6lOSxSb7-mw7GDQP3jal2PU5hdLACSub7_0bPSL1O3mZfeySno5ID8-gQQ-tSbForVPIaSpu8bAHXVS6GfDQ4oQZMcZmUz5_BQ5sxyzM1P9y37Qh2X2sV_MYzjxIgspWCeMGYLOTJ7JJbwSET6oM4SOgEyXRqgh4qkrNw1Sv16cYHon4GB3o-s_I_R8cHT8oXLldxJ8VJv3H_LT3ARduTG_t8lRWytvcVtnzn_DO-qYLFcJ_6KGY0BujN1JXpXDPU22DRNuIdIFmNUTvcQ4ksx_qsmmsd6QK7EmZCtbZeo3tM-VcRwWtXp17m3A92a7Ok23HAELBKyrA";

class JwtToken {
  static String getJwt() {
    if (tempJwt.isNotEmpty) {
      return tempJwt;
    }

    // be sure you have the correct values in the Config class

    final jwt = JWT(
      {
        'aud_fingerprints':
            "sha256/Ua7bETOKCoTa3LUStNQlrH6LIwuItZgfJjzfY70J5h0=",
        'ksk_pin': 'sha256/1Zna4T6PKcJ3Kq/dbVylb8n62j/AdQYUzWrj/4sk5Q8=',
        'usr': Config.username
      },
      audience: Audience([Config.host]),
      issuer: Config.issuer,
      subject: Config.merchantId,
    );

    final key = RSAPrivateKey(Config.privateKeyPem);
    final token = jwt.sign(key, algorithm: JWTAlgorithm.RS512);

    return token;
  }
}
