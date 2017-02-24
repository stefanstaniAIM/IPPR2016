(function () {
    'use strict';

    angular.module('storage.modelerStorage', []).factory('modelerStorage', modelerStorage);

    /** @ngInject */
    function modelerStorage($log, local) {

        var service = {};

        service.init = function () {
            $log.info('modelerStorage - init()');

            var modelerSettings = {
                selectedView: 'SID',
                selectedSubject: {},
                selectedConnection: {},
                sidJson: {},
                sbdJsonArray: [],
                businessObjects: []
            };

            if (local.get('modelerSettings') === null) {
                //$log.debug('modelerStorage - init() - create modelerSettings');
                local.set('modelerSettings', modelerSettings);
            } else {
                //$log.debug('modelerStorage - init() - modelerSettings already created');
            }
        };

        service.getModelerSettings = function () {
            return local.get('modelerSettings');
        };

        service.getSelectedView = function () {
            return local.get('modelerSettings').selectedView;
        };

        service.setSelectedView = function (selectedView) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.selectedView = selectedView;
            local.set('modelerSettings', modelerSettings);
        };

        service.getSelectedSubject = function () {
            return local.get('modelerSettings').selectedSubject;
        };

        service.setSelectedSubject = function (selectedSubject) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.selectedSubject = selectedSubject;
            local.set('modelerSettings', modelerSettings);
        };

        service.getSelectedConnection = function () {
            return local.get('modelerSettings').selectedConnection;
        };

        service.setSelectedConnection = function (selectedConnection) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.selectedConnection = selectedConnection;
            local.set('modelerSettings', modelerSettings);
        };

        service.getSidJson = function () {
            return local.get('modelerSettings').sidJson;
        };

        service.setSidJson = function (sidJson) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.sidJson = sidJson;
            local.set('modelerSettings', modelerSettings);
        };

        service.removeFromSidJsonById = function (objectId) {
            var modelerSettings = local.get('modelerSettings');

            var object = modelerSettings.sidJson.cells.find(function (el) {
                return el.id === objectId
            });

            if (typeof object !== 'undefined') {
                var objectIndex = modelerSettings.sidJson.cells.indexOf(object);
                modelerSettings.sidJson.cells.splice(objectIndex, 1);
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.getSbdJsonArray = function () {
            return local.get('modelerSettings').sbdJsonArray;
        };

        service.setSbdJsonArray = function (sbdJsonArray) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.sbdJsonArray = sbdJsonArray;
            local.set('modelerSettings', modelerSettings);
        };

        service.addToSbdJsonArray = function (object) {
            var modelerSettings = local.get('modelerSettings');

            var arrayObject = modelerSettings.sbdJsonArray.find(function (obj) {
                return obj.id === object.id;
            });

            if (typeof arrayObject !== 'undefined') {
                var arrayObjectIndex = modelerSettings.sbdJsonArray.indexOf(arrayObject);
                modelerSettings.sbdJsonArray.splice(arrayObjectIndex, 1);
                modelerSettings.sbdJsonArray.push(object)
            } else {
                modelerSettings.sbdJsonArray.push(object);
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.getSbdJsonBySubjectId = function (subjectId) {
            var modelerSettings = local.get('modelerSettings');

            var result = modelerSettings.sbdJsonArray.find(function (object) {
                return object.id === subjectId;
            });

            return result.sbdJson;
        };

        service.subjectContainsSbdJson = function (subjectId) {
            var modelerSettings = local.get('modelerSettings');
            var result = modelerSettings.sbdJsonArray.find(function (object) {
                return object.id === subjectId;
            });

            if (typeof result === 'undefined' || _.isEmpty(result.sbdJson.cells)) {
                return false;
            }

            return true;
        };

        service.getSubjectDisplayItems = function () {
            var modelerSettings = local.get('modelerSettings');

            var subjectDisplayItems = [];

            if (!_.isEmpty(modelerSettings.sidJson) && !_.isEmpty(modelerSettings.sidJson.cells)) {
                local.get('modelerSettings').sidJson.cells.forEach(function (subject) {
                    if (subject.type === 'sid.SubjectElement') {
                        subjectDisplayItems.push({
                            id: subject.id,
                            value: subject.customAttrs.name.toLowerCase(),
                            display: subject.customAttrs.name
                        });
                    }
                });
            }

            return subjectDisplayItems;
        };

        service.getBusinessObjects = function () {
            return local.get('modelerSettings').businessObjects;
        };

        service.setBusinessObjects = function (businessObjects) {
            var modelerSettings = local.get('modelerSettings');
            modelerSettings.businessObjects = businessObjects;
            local.set('modelerSettings', modelerSettings);
        };

        service.addBusinessObject = function (object) {
            var modelerSettings = local.get('modelerSettings');

            var arrayObject = modelerSettings.businessObjects.find(function (obj) {
                return obj.id === object.id;
            });

            if (typeof arrayObject !== 'undefined') {
                var arrayObjectIndex = modelerSettings.businessObjects.indexOf(arrayObject);
                modelerSettings.businessObjects.splice(arrayObjectIndex, 1);
                modelerSettings.businessObjects.push(object)
            } else {
                modelerSettings.businessObjects.push(object);
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.removeBusinessObject = function (objectId) {
            var modelerSettings = local.get('modelerSettings');

            var arrayObject = modelerSettings.businessObjects.find(function (obj) {
                return obj.id === objectId;
            });

            if (typeof arrayObject !== 'undefined') {
                var arrayObjectIndex = modelerSettings.businessObjects.indexOf(arrayObject);
                modelerSettings.businessObjects.splice(arrayObjectIndex, 1);
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.removeSbdJsonBySubjctId = function (subjectId) {
            var modelerSettings = local.get('modelerSettings');

            var arrayObject = modelerSettings.sbdJsonArray.find(function (obj) {
                return obj.id === subjectId;
            });

            if (typeof arrayObject !== 'undefined') {
                var arrayObjectIndex = modelerSettings.sbdJsonArray.indexOf(arrayObject);
                modelerSettings.sbdJsonArray.splice(arrayObjectIndex, 1);
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.removeFromSbdJsonArrayBySubjectId = function (subjectId, objectId) {
            var modelerSettings = local.get('modelerSettings');

            var arrayObject = modelerSettings.sbdJsonArray.find(function (obj) {
                return obj.id === subjectId;
            });
            var arrayObjectIndex = modelerSettings.sbdJsonArray.indexOf(arrayObject);

            if (typeof arrayObject !== 'undefined') {

                _.remove(modelerSettings.sbdJsonArray[arrayObjectIndex].sbdJson.cells, function(obj) {
                    return obj.id === objectId;
                });
            }

            local.set('modelerSettings', modelerSettings);
        };

        service.modelerSettingsValid =function () {
          $log.debug(local.get('modelerSettings'));
        };

        service.clear = function () {
            local.removeItem('modelerSettings');
        };

        service.init();

        return service;
    }

})();
