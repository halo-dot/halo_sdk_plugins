#import "SdkflutterpluginPlugin.h"
#if __has_include(<sdkflutterplugin/sdkflutterplugin-Swift.h>)
#import <sdkflutterplugin/sdkflutterplugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "sdkflutterplugin-Swift.h"
#endif

@implementation SdkflutterpluginPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSdkflutterpluginPlugin registerWithRegistrar:registrar];
}
@end
