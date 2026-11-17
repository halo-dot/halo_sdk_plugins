using System.Buffers.Text;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json;

namespace HaloDot.SdkUi.Example;

/// <summary>Signs the JWT the SDK asks for. For this example only: a real host should get the JWT in their way.</summary>
public static class JwtToken
{
    public static async Task<string> CreateAsync()
    {
        var header = new { alg = "RS512", typ = "JWT" };
        var issuedAt = DateTimeOffset.UtcNow;
        var payload = new
        {
            iat = issuedAt.ToUnixTimeSeconds(),
            exp = issuedAt.AddMinutes(15).ToUnixTimeSeconds(),
            iss = Config.Issuer,
            sub = Config.MerchantId,
            aud = new[] { Config.Host },
            aud_fingerprints = Config.Aud,
            ksk_pin = Config.Ksk,
            usr = Config.Username,
        };

        var signingInput = $"{Encode(JsonSerializer.SerializeToUtf8Bytes(header))}.{Encode(JsonSerializer.SerializeToUtf8Bytes(payload))}";

        using var rsa = RSA.Create();
        rsa.ImportFromPem(await PrivateKeyPemAsync());
        var signature = rsa.SignData(Encoding.UTF8.GetBytes(signingInput), HashAlgorithmName.SHA512, RSASignaturePadding.Pkcs1);
        return $"{signingInput}.{Encode(signature)}";
    }

    private static async Task<string> PrivateKeyPemAsync()
    {
        try
        {
            await using var stream = await FileSystem.OpenAppPackageFileAsync("private_key.pem");
            using var reader = new StreamReader(stream);
            return await reader.ReadToEndAsync();
        }
        catch (FileNotFoundException)
        {
            throw new InvalidOperationException("Save your private key from the Halo Developer Portal as private_key.pem next to Config.cs.");
        }
    }

    private static string Encode(byte[] bytes) => Base64Url.EncodeToString(bytes);
}
