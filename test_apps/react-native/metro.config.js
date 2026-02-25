const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const path = require('path');

// Point Metro at the local plugin so we can import 'halo-sdk-react-native'
const pluginPath = path.resolve(__dirname, '../../plugins/react-native');
const appNodeModules = path.resolve(__dirname, 'node_modules');

const config = {
  watchFolders: [pluginPath],
  resolver: {
    extraNodeModules: {
      'halo-sdk-react-native': pluginPath,
    },
    // When resolving modules from within the plugin (which has no node_modules),
    // fall back to the test app's node_modules so peer deps like react-native resolve.
    nodeModulesPaths: [appNodeModules],
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
