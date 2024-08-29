## Getting started for developers

### Environment Setup

See [Environment](./README.md#environment).

### Things to know

- The codebase uses MethodChannels to trigger native functions from the Flutter side.
- Although we could have used MethodChannels to send callback messages from native code to Flutter, we use EventChannels (no particular reason, just a tried and tested method)
- The Channels that pass data between the two platforms accepts a limited list of data types.
- Importtant to note that Lists and Maps should always be of type `dynamic` when passing data from native to dart.
- Although little documentation exists about EventChannels, they too follow most of the rules of MethodChannels.

Read all about it [here](https://docs.flutter.dev/platform-integration/platform-channels).

### Plugin Development

- The example app will build the code from the plugin folder, no need to build the plugin separately.

### Push changes to the public remote

First you need to install [git-filter-repo](https://github.com/newren/git-filter-repo), if you have Homebrew just run `brew install git-filter-repo` and ensure you have `python >= 3.6`. Otherwise you can follow the steps on the link.

Run `scripts/push_plugins_to_public_repo.sh`. Use the `--help` to understand how to use it.

Here is what the script does (incase you want to do it manually):

1. It clones a new copy of the `halo_mpos` repo, checkout the branch with the latest code you want to push.

2. In the new repo, it runs this `git filter-repo --path plugins --path test_apps --force` to remove all the commit but the onces that touch these folders.

4. It ddoes a `git remote add public git@github.com:halo-dot/halo_sdk_plugins.git`

5. Then Checkouts a branch that exists on the remote you've just added, e.g. `git checkout main`

6. Then finally it pushes the code with `git push public <branch>` where <branch> is the branch name that exists on remote.
