#!/usr/bin/env node

var fs = require('fs'),
    path = require('path'),
    xml2js = require('xml2js'),
    parser = new xml2js.Parser();

module.exports = function (context) {
    console.log("Starting override_activity.js hook")
    var deferral = require('q').defer();

    var PACKAGE_NAME_PLACEHOLDER = "<%PACKAGE_NAME%>";

    var packageName, activityTargetPath;

    var projectRoot = context.opts.projectRoot;
    var platformRoot = path.join(projectRoot, 'platforms/android');

    var activitySourcePath = path.join(context.opts.plugin.pluginInfo.dir, 'src/android/MainActivity.java');
    console.log("activitySourcePath: "+activitySourcePath);

    var configXmlPath =  path.join(projectRoot, 'config.xml');

    fs.readFile(configXmlPath, function(err, data) {
        if(err){
            deferral.reject(new Error("Failed to read config.xml: " + err));
        }
        parser.parseString(data, function (err, result) {
            if(err){
                deferral.reject(new Error("Failed to parse config.xml: " + err));
            }
            packageName = result.widget.$.id;
            activityTargetPath = path.join(platformRoot, 'app/src/main/java', packageName.replace(/\./g,'/'), 'MainActivity.java');
            console.log("activityTargetPath: "+activityTargetPath);

            fs.readFile(activitySourcePath, function(err, activitySrc) {
                if (err) {
                    deferral.reject(new Error("Failed to read source MainActivity.java: " + err))
                }
                activitySrc = activitySrc.toString().replace(PACKAGE_NAME_PLACEHOLDER, packageName);
                fs.writeFile(activityTargetPath, activitySrc, 'utf8', function (err) {
                    if (err) {
                        deferral.reject(new Error("Failed to write target MainActivity.java: " + err));
                    }
                    deferral.resolve();
                    console.log("SUCCESS!");
                });
            });

        });
    });
    return deferral.promise;
};