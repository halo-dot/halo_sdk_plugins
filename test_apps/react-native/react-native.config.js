module.exports = {
  dependencies: {
    // Disable auto-linking for the Halo SDK React Native plugin.
    // The Android project is manually included in settings.gradle as
    // ':halo-sdk-react-native', pointing to the local source in
    // plugins/react-native/android. This prevents the duplicate class
    // error that occurs when auto-linking also includes the npm package.
    'halo-sdk-react-native': {
      platforms: {
        android: null,
      },
    },
  },
};
