(function () {
    'use strict';

    angular.module('storage.owlService', []).factory('owlService', owlService);

    /** @ngInject */
    function owlService($log, modelerStorage) {

        var service = {};

        service.vacationProcess = '{"selectedView":"SBD","selectedSubject":{"type":"sid.SubjectElement","position":{"x":471,"y":589},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Boss","startSubject":false},"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","z":45,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":1}}},"selectedConnection":{},"sidJson":{"cells":[{"type":"sid.SubjectElement","position":{"x":471,"y":91},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Employee","startSubject":false},"id":"50d69cdc-b314-478c-8808-eb056d34554b","z":10,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":1}}},{"type":"sid.MessageConnectorElement","source":{"id":"50d69cdc-b314-478c-8808-eb056d34554b"},"target":{"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d"},"customAttrs":{"isBidirectional":true,"sourceToTarget":{"messageTypes":[{"name":"Vacation Request","id":"k3m36qnea","$$hashKey":"object:351"}]},"targetToSource":{"messageTypes":[{"name":"Vacation Request OK","id":"5e3cl0mnb","$$hashKey":"object:405"},{"name":"Vacation Request NOK","id":"9pqm8l7ji","$$hashKey":"object:406"}]}},"id":"9f8a4d92-dd3d-4e1a-95bc-41a55399c2c5","z":44,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"},".marker-source":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sid.SubjectElement","position":{"x":471,"y":589},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Boss","startSubject":false},"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","z":45,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":1}}}]},"sbdJsonArray":[{"id":"50d69cdc-b314-478c-8808-eb056d34554b","sbdJson":{"cells":[{"type":"sbd.SendStateElement","position":{"x":804,"y":78},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Send Vacation Request","startState":false,"endState":false},"id":"9e8595af-4cb3-4b6e-a5c5-34d409bd322c","z":75,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":0}}},{"type":"sbd.ReceiveStateElement","position":{"x":1275,"y":83},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Receive Vacation Request Response","startState":false,"endState":false},"id":"051da1f1-2b0f-4076-9ab2-d3b4f6cc53ef","z":211,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":0}}},{"type":"sbd.FunctionStateElement","position":{"x":780,"y":380},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Realize Vacation Request OK","startState":false,"endState":false},"id":"dbda1a03-8f5c-42e7-9c00-37548d9e06e2","z":390,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.FunctionStateElement","position":{"x":1347,"y":376},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Realize Vacation Request NOK","startState":false,"endState":false},"id":"77bbd669-44f7-4c7e-86ef-a93413460a0a","z":397,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.FunctionStateElement","position":{"x":383,"y":76},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Create Vacation Request","startState":true,"endState":false},"id":"a036758b-6fb2-4b42-9487-c9e4aaabcc53","z":417,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.FunctionStateElement","position":{"x":1064,"y":648},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"End","startState":false,"endState":true},"id":"1f1c4b4e-cac7-416d-b665-e4a2cd59bef7","z":430,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.FromFunctionStateConnectorElement","source":{"id":"a036758b-6fb2-4b42-9487-c9e4aaabcc53"},"target":{"id":"9e8595af-4cb3-4b6e-a5c5-34d409bd322c"},"customAttrs":{"transitionName":"done"},"id":"a53caf88-4598-4060-9355-cb2717e1c6bb","z":431,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromSendStateConnectorElement","source":{"id":"9e8595af-4cb3-4b6e-a5c5-34d409bd322c"},"target":{"id":"051da1f1-2b0f-4076-9ab2-d3b4f6cc53ef"},"customAttrs":{"toSubject":{"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","value":"boss","display":"Boss"},"toMessage":{"id":"k3m36qnea","name":"Vacation Request"}},"id":"e75f2dfe-1f0e-4e1b-a84d-8d11c8f85434","z":432,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromReceiveStateConnectorElement","source":{"id":"051da1f1-2b0f-4076-9ab2-d3b4f6cc53ef"},"target":{"id":"dbda1a03-8f5c-42e7-9c00-37548d9e06e2"},"customAttrs":{"fromSubject":{"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","value":"boss","display":"Boss"},"fromMessage":{"id":"5e3cl0mnb","name":"Vacation Request OK"}},"id":"7f2a04fa-c44f-4258-8304-c660653db404","z":433,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromReceiveStateConnectorElement","source":{"id":"051da1f1-2b0f-4076-9ab2-d3b4f6cc53ef"},"target":{"id":"77bbd669-44f7-4c7e-86ef-a93413460a0a"},"customAttrs":{"fromSubject":{"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","value":"boss","display":"Boss"},"fromMessage":{"id":"9pqm8l7ji","name":"Vacation Request NOK"}},"id":"5fedb0e9-f813-44f4-87d9-c0cc5ef5496f","z":434,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromFunctionStateConnectorElement","source":{"id":"dbda1a03-8f5c-42e7-9c00-37548d9e06e2"},"target":{"id":"1f1c4b4e-cac7-416d-b665-e4a2cd59bef7"},"customAttrs":{"transitionName":"done"},"id":"db381fd2-5f94-4c05-a7db-126ea712be75","z":435,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromFunctionStateConnectorElement","source":{"id":"77bbd669-44f7-4c7e-86ef-a93413460a0a"},"target":{"id":"1f1c4b4e-cac7-416d-b665-e4a2cd59bef7"},"customAttrs":{"transitionName":"done"},"id":"72ac84eb-9b45-4878-a3b8-05f166a31440","z":436,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}}]}},{"id":"bbd02f03-743b-40e8-ad1c-dbdf045e9d9d","sbdJson":{"cells":[{"type":"sbd.ReceiveStateElement","position":{"x":358,"y":46},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Receive Vacation Request","startState":true,"endState":false},"id":"7a96abf5-b374-411b-b574-87947b0a091c","z":7,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":0}}},{"type":"sbd.FunctionStateElement","position":{"x":979,"y":46},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Vacation Request OK or NOK","startState":false,"endState":false},"id":"4226e6b5-f9d6-4ddd-9964-56e25b2e4889","z":120,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.FunctionStateElement","position":{"x":963,"y":648},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"End","startState":false,"endState":true},"id":"d8b8262d-826f-41bf-8b5b-862751df6f10","z":174,"attrs":{"rect":{"cursor":"pointer"}}},{"type":"sbd.SendStateElement","position":{"x":1222,"y":311},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Vacation Request NOK","startState":false,"endState":false},"id":"494cafdf-54f7-4d29-974e-1773e00fbb1f","z":226,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":0}}},{"type":"sbd.SendStateElement","position":{"x":627,"y":320},"size":{"width":150,"height":130},"angle":0,"customAttrs":{"name":"Vacation Request OK","startState":false,"endState":false},"id":"7948ef96-c87a-4fbd-9ac5-200647fcbef2","z":300,"attrs":{"rect":{"fill":"blue","stroke":"none","cursor":"pointer","fill-opacity":0}}},{"type":"sbd.FromSendStateConnectorElement","source":{"id":"494cafdf-54f7-4d29-974e-1773e00fbb1f"},"target":{"id":"d8b8262d-826f-41bf-8b5b-862751df6f10"},"customAttrs":{"toSubject":{"id":"50d69cdc-b314-478c-8808-eb056d34554b","value":"employee","display":"Employee"},"toMessage":{"id":"9pqm8l7ji","name":"Vacation Request NOK"}},"id":"8921ff2e-11c8-4160-8231-da3e8e6f3b3d","z":316,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromReceiveStateConnectorElement","source":{"id":"7a96abf5-b374-411b-b574-87947b0a091c"},"target":{"id":"4226e6b5-f9d6-4ddd-9964-56e25b2e4889"},"customAttrs":{"fromSubject":{"id":"50d69cdc-b314-478c-8808-eb056d34554b","value":"employee","display":"Employee"},"fromMessage":{"id":"k3m36qnea","name":"Vacation Request"}},"id":"1ada0e8b-530d-4b80-896b-fd415ecf4e4f","z":312,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromFunctionStateConnectorElement","source":{"id":"4226e6b5-f9d6-4ddd-9964-56e25b2e4889"},"target":{"id":"7948ef96-c87a-4fbd-9ac5-200647fcbef2"},"customAttrs":{"transitionName":"done"},"id":"d1f125b0-3c0d-47ab-820d-71e2a6c85dee","z":313,"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromFunctionStateConnectorElement","source":{"id":"4226e6b5-f9d6-4ddd-9964-56e25b2e4889"},"target":{"id":"494cafdf-54f7-4d29-974e-1773e00fbb1f"},"customAttrs":{"transitionName":"done"},"id":"4fa20ab8-ee3a-4932-9bd3-8fd6dd22d7eb","z":314,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}},{"type":"sbd.FromSendStateConnectorElement","source":{"id":"7948ef96-c87a-4fbd-9ac5-200647fcbef2"},"target":{"id":"d8b8262d-826f-41bf-8b5b-862751df6f10"},"customAttrs":{"toSubject":{"id":"50d69cdc-b314-478c-8808-eb056d34554b","value":"employee","display":"Employee"},"toMessage":{"id":"5e3cl0mnb","name":"Vacation Request OK"}},"id":"aa39fd08-c909-42b0-93e0-a3539bc92079","z":315,"vertices":[],"attrs":{".connection":{"stroke":"#222138","stroke-width":3},".connection-wrap":{"fill":"yellow","d":"M 10 0 L 0 5 L 10 10 z"},".marker-target":{"fill":"#000000","d":"M 10 0 L 0 5 L 10 10 z"}}}]}}],"businessObjects":[{"name":"Vacation Request","id":"k3m36qnea"},{"name":"Vacation Request OK","id":"5e3cl0mnb"},{"name":"Vacation Request NOK","id":"9pqm8l7ji"}]}';

        service.sidJson = null;
        service.sbdJsonArray = null;

        service.messages = [];

        service.randomNumber1 = 1;
        service.randomNumber2 = 1;

        service.oneTab = '  ';
        service.twoTabs = '      ';

        service.init = function () {
            $log.info('owlService - init()');
        };

        service.getStoredData = function () {
            service.messages = [];
            service.sidJson = modelerStorage.getSidJson();
            service.sbdJsonArray = modelerStorage.getSbdJsonArray();
        };

        service.downloadOwlFile = function (processName) {
            //$log.debug('download owl');

            service.getStoredData();

            //$log.debug(JSON.parse(service.vacationProcess));

            //var vacationProcess = JSON.parse(service.vacationProcess);

            //service.sidJson = vacationProcess.sidJson;
            //service.sbdJsonArray = vacationProcess.sbdJsonArray;

            var template = '<?xml version="1.0"?>\n' +
                '<!DOCTYPE rdf:RDF [\n' +
                '   <!ENTITY owl "http://www.w3.org/2002/07/owl#" >\n' +
                '   <!ENTITY xsd "http://www.w3.org/2001/XMLSchema#" >\n' +
                '   <!ENTITY rdfs "http://www.w3.org/2000/01/rdf-schema#" >\n' +
                '   <!ENTITY abstract-pass-ont "http://www.imi.kit.edu/abstract-pass-ont#" >\n' +
                '   <!ENTITY standard-pass-ont "http://www.imi.kit.edu/standard-pass-ont#" >\n' +
                '   <!ENTITY rdf "http://www.w3.org/1999/02/22-rdf-syntax-ns#" >\n' +
                ']>\n' +
                '<rdf:RDF xmlns:abstract-pass-ont="http://www.imi.kit.edu/abstract-pass-ont#" xmlns:standard-pass-ont="http://www.imi.kit.edu/standard-pass-ont#" xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" xmlns:owl="http://www.w3.org/2002/07/owl#" xmlns:xsd="http://www.w3.org/2001/XMLSchema#" xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#" xmlns="http://www.imi.kit.edu/s-bpm/exampleProcess">\n' +
                '   <owl:Ontology rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess">\n' +
                '       <owl:imports rdf:resource="http://www.imi.kit.edu/abstract-pass-ont"></owl:imports>\n' +
                '       <owl:imports rdf:resource="http://www.imi.kit.edu/standard-pass-ont"></owl:imports>\n' +
                '   </owl:Ontology>\n' +
                service.generateProcessBasicInfo(processName) + '\n' +
                service.generateSIDInfo() + '\n' +
                service.generateSIDElementInfo() + '\n' +
                service.generateSBDElementInfo() + '\n' +
                '</rdf:RDF>';

            service.download(processName+'.owl', template);

            service.messages = [];

        };

        service.download = function (filename, text) {
            var pom = document.createElement('a');
            pom.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
            pom.setAttribute('download', filename);

            if (document.createEvent) {
                var event = document.createEvent('MouseEvents');
                event.initEvent('click', true, true);
                pom.dispatchEvent(event);
            }
            else {
                pom.click();
            }
        };

        service.generateRandomNumbers = function () {

            var currentRandonNumber1 = service.randomNumber1;
            var currentRandonNumber2 = service.randomNumber2;

            service.randomNumber1 = generateRandom();
            service.randomNumber2 = generateRandom();

            if (service.randomNumber1 === currentRandonNumber1 || service.randomNumber2 === currentRandonNumber2) {
                service.generateRandomNumbers();
            }

            function generateRandom() {
                return Math.floor((Math.random() * 1000) + 1);
            }

        };

        service.generateProcessBasicInfo = function (processName) {
            //$log.debug('generate process basic info');

            service.generateRandomNumbers();

            var processBasicInfo = '';

            var subjects = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.SubjectElement';
            });
            var messages = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.MessageConnectorElement';
            });

            processBasicInfo = service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#'+processName+'" >\n' +
                service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;PASSProcessModel" ></rdf:type>\n' +
                service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >Layer_0</standard-pass-ont:hasModelComponentID>\n' +
                service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >'+processName+'</standard-pass-ont:hasModelComponentLable>\n' +
                service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID" ></standard-pass-ont:hasModelComponent>\n';

            subjects.forEach(function (sb) {
                processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + sb.id + '" ></standard-pass-ont:hasModelComponent>\n';
            });

            messages.forEach(function (msg) {
                //sourceToTarget
                var sttMessageTypes = msg.customAttrs.sourceToTarget.messageTypes;
                sttMessageTypes.forEach(function (msgType) {
                    processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber1+'_' + msg.id + '_message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                });

                var ttsMessageTypes = msg.customAttrs.targetToSource.messageTypes;
                ttsMessageTypes.forEach(function (msgType) {
                    processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber2+'_' + msg.id + '_message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                });
            });

            subjects.forEach(function (sb) {
                processBasicInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_SID_subject_' + sb.id + '" ></standard-pass-ont:hasModelComponent>\n';
            });

            processBasicInfo += service.oneTab + '</owl:NamedIndividual>';

            //$log.debug(service.processBasicInfo);
            return processBasicInfo;
        };

        service.generateSIDInfo = function () {
            //$log.debug('generate sid info');

            var sidInfo = '';

            var subjects = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.SubjectElement';
            });
            var messages = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.MessageConnectorElement';
            });

            sidInfo = service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SID" >\n' +
                service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;Layer" ></rdf:type>\n' +
                service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SID</standard-pass-ont:hasModelComponentID>\n' +
                service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >SID</standard-pass-ont:hasModelComponentLable>\n';

            subjects.forEach(function (sb) {
                sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + sb.id + '" ></standard-pass-ont:hasModelComponent>\n';
            });

            messages.forEach(function (msg) {
                //sourceToTarget
                var sttMessageTypes = msg.customAttrs.sourceToTarget.messageTypes;
                sttMessageTypes.forEach(function (msgType) {
                    sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber1+'_' + msg.id + '_message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    service.messages.push({
                        messageId: msgType.id,
                        messageOwlIdentifier: 'SID_messageConnector_'+service.randomNumber1+'_' + msg.id + '_message_' + msgType.id
                    });
                });

                var ttsMessageTypes = msg.customAttrs.targetToSource.messageTypes;
                ttsMessageTypes.forEach(function (msgType) {
                    sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber2+'_' + msg.id + '_message_' + msgType.id + '" ></standard-pass-ont:hasModelComponent>\n';
                    service.messages.push({
                        messageId: msgType.id,
                        messageOwlIdentifier: 'SID_messageConnector_'+service.randomNumber2+'_' + msg.id + '_message_' + msgType.id
                    });
                });
            });

            subjects.forEach(function (sb) {
                sidInfo += service.twoTabs + '<standard-pass-ont:hasModelComponent rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_SID_subject_' + sb.id + '" ></standard-pass-ont:hasModelComponent>\n';
            });

            sidInfo += service.oneTab + '</owl:NamedIndividual>';

            return sidInfo;
        };

        service.generateSIDElementInfo = function () {
            //$log.debug('generate sid element info');

            var sidElementInfo = '';

            var subjects = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.SubjectElement';
            });
            var messages = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.MessageConnectorElement';
            });

            subjects.forEach(function (sb) {
                sidElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + sb.id + '" >\n';
                sidElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;SingleActor" ></rdf:type>\n';
                sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SID_subject_' + sb.id + '</standard-pass-ont:hasModelComponentID>\n';
                sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >' + sb.customAttrs.name + '</standard-pass-ont:hasModelComponentLable>\n';
                sidElementInfo += service.twoTabs + '<standard-pass-ont:hasBehavior rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_SID_' + sb.id + '" ></standard-pass-ont:hasBehavior>\n';
                sidElementInfo += service.oneTab + '</owl:NamedIndividual>\n';
            });

            messages.forEach(function (msg) {
                //sourceToTarget
                var sttMessageTypes = msg.customAttrs.sourceToTarget.messageTypes;
                sttMessageTypes.forEach(function (msgType) {
                    sidElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" >\n';
                    sidElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;MessageSpec" ></rdf:type>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >message_' + msgType.id + '</standard-pass-ont:hasModelComponentID>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >' + msgType.name + '</standard-pass-ont:hasModelComponentLable>\n';
                    sidElementInfo += service.oneTab + '</owl:NamedIndividual>\n';

                    sidElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber1+'_' + msg.id + '_message_' + msgType.id + '" >\n';
                    sidElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;StandardMessageExchange" ></rdf:type>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SID_messageConnector_'+service.randomNumber1+'_' + msg.id + '_message_' + msgType.id + '</standard-pass-ont:hasModelComponentID>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >Message:' + msgType.name + ' From: SID_subject' + msg.source.id + ' To: SID_subject' + msg.target.id + '</standard-pass-ont:hasModelComponentLable>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasSender rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + msg.source.id + '" ></standard-pass-ont:hasSender>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasReceiver rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + msg.target.id + '" ></standard-pass-ont:hasReceiver>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasMessageType rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasMessageType>\n';
                    sidElementInfo += service.oneTab + '</owl:NamedIndividual>\n';
                });

                var ttsMessageTypes = msg.customAttrs.targetToSource.messageTypes;
                ttsMessageTypes.forEach(function (msgType) {
                    sidElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" >\n';
                    sidElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;MessageSpec" ></rdf:type>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >message_' + msgType.id + '</standard-pass-ont:hasModelComponentID>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >' + msgType.name + '</standard-pass-ont:hasModelComponentLable>\n';
                    sidElementInfo += service.oneTab + '</owl:NamedIndividual>\n';

                    sidElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_messageConnector_'+service.randomNumber2+'_' + msg.id + '_message_' + msgType.id + '" >\n';
                    sidElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;StandardMessageExchange" ></rdf:type>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SID_messageConnector_'+service.randomNumber2+'_' + msg.id + '_message_' + msgType.id + '</standard-pass-ont:hasModelComponentID>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >Message:' + msgType.name + ' From: SID_subject' + msg.target.id + ' To: SID_subject' + msg.source.id + '</standard-pass-ont:hasModelComponentLable>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasSender rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + msg.target.id + '" ></standard-pass-ont:hasSender>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasReceiver rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SID_subject_' + msg.source.id + '" ></standard-pass-ont:hasReceiver>\n';
                    sidElementInfo += service.twoTabs + '<standard-pass-ont:hasMessageType rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#message_' + msgType.id + '" ></standard-pass-ont:hasMessageType>\n';
                    sidElementInfo += service.oneTab + '</owl:NamedIndividual>\n';
                });
            });

            return sidElementInfo;
        };

        service.generateSBDElementInfo = function () {
            //$log.debug('generate sbd element info');

            var sbdElementInfo = '';

            var subjects = service.sidJson.cells.filter(function (obj) {
                return obj.type === 'sid.SubjectElement';
            });

            subjects.forEach(function (sb) {
                sbdElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_SID_' + sb.id + '" >\n';
                sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;Behavior" ></rdf:type>\n';
                sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SBD_SID_' + sb.id + '</standard-pass-ont:hasModelComponentID>\n';
                sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >SBD: ' + sb.customAttrs.name + '</standard-pass-ont:hasModelComponentLable>\n';

                var sbBehavior = service.sbdJsonArray.find(function (bh) {
                    return bh.id === sb.id;
                });

                //Generate general info
                if (typeof sbBehavior !== 'undefined') {
                    var states = sbBehavior.sbdJson.cells.filter(function (obj) {
                        return obj.type === 'sbd.ReceiveStateElement' || obj.type === 'sbd.SendStateElement' || obj.type === 'sbd.FunctionStateElement';
                    });

                    var edges = sbBehavior.sbdJson.cells.filter(function (obj) {
                        return obj.type === 'sbd.FromFunctionStateConnectorElement' || obj.type === 'sbd.FromSendStateConnectorElement' || obj.type === 'sbd.FromReceiveStateConnectorElement';
                    });

                    states.forEach(function (state) {
                        if (state.customAttrs.startState) {
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasInitialState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_' + state.id + '" ></standard-pass-ont:hasInitialState>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_' + state.id + '" ></standard-pass-ont:hasState>\n';
                        } else if (state.customAttrs.endState) {
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasEndState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_' + state.id + '" ></standard-pass-ont:hasEndState>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_' + state.id + '" ></standard-pass-ont:hasState>\n';
                        } else {
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_' + state.id + '" ></standard-pass-ont:hasState>\n';
                        }
                    });

                    edges.forEach(function (edge) {
                        sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasEdge rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_edge_' + edge.id + '" ></standard-pass-ont:hasEdge>\n';
                    });
                }

                sbdElementInfo += service.oneTab + '</owl:NamedIndividual>\n';

                //Generate info for every single state/transition
                if (typeof sbBehavior !== 'undefined') {
                    var states = sbBehavior.sbdJson.cells.filter(function (obj) {
                        return obj.type === 'sbd.ReceiveStateElement' || obj.type === 'sbd.SendStateElement' || obj.type === 'sbd.FunctionStateElement';
                    });

                    var edges = sbBehavior.sbdJson.cells.filter(function (obj) {
                        return obj.type === 'sbd.FromFunctionStateConnectorElement' || obj.type === 'sbd.FromSendStateConnectorElement' || obj.type === 'sbd.FromReceiveStateConnectorElement';
                    });

                    states.forEach(function (state) {

                        sbdElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+state.id+'" >\n';

                        if (state.type === 'sbd.SendStateElement') {
                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;SendState" ></rdf:type>\n';
                        } else if (state.type === 'sbd.ReceiveStateElement') {
                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;ReceiveState" ></rdf:type>\n';
                        } else if (state.type === 'sbd.FunctionStateElement') {
                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;FunctionState" ></rdf:type>\n';
                        }

                        sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SBD_state_'+state.id+'</standard-pass-ont:hasModelComponentID>\n';
                        sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >'+state.customAttrs.name+'</standard-pass-ont:hasModelComponentLable>\n';

                        if (state.customAttrs.startState) {
                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;InitialState" ></rdf:type>\n';
                        } else if (state.customAttrs.endState) {
                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;EndState" ></rdf:type>\n';
                        }

                        sbdElementInfo += service.oneTab + '</owl:NamedIndividual>\n';
                    });

                    edges.forEach(function (edge) {
                        sbdElementInfo += service.oneTab + '<owl:NamedIndividual rdf:about="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_edge_'+edge.id+'" >\n';

                        if (edge.type === 'sbd.FromSendStateConnectorElement') {

                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;SendTransition" ></rdf:type>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SBD_edge_'+edge.id+'</standard-pass-ont:hasModelComponentID>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >To: '+edge.customAttrs.toSubject.display+ ' Msg: ' + edge.customAttrs.toMessage.name + '</standard-pass-ont:hasModelComponentLable>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasSourceState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.source.id+'" ></standard-pass-ont:hasSourceState>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasTargetState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.target.id+'" ></standard-pass-ont:hasTargetState>\n';

                            //Message
                            var message = service.messages.find(function (msg) {
                                return msg.messageId === edge.customAttrs.toMessage.id;
                            });

                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:refersTo rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#'+message.messageOwlIdentifier+'" ></standard-pass-ont:refersTo>\n';

                        } else if (edge.type === 'sbd.FromReceiveStateConnectorElement') {

                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;ReceiveTransition" ></rdf:type>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SBD_edge_'+edge.id+'</standard-pass-ont:hasModelComponentID>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >From: '+edge.customAttrs.fromSubject.display+ ' Msg: ' + edge.customAttrs.fromMessage.name + '</standard-pass-ont:hasModelComponentLable>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasSourceState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.source.id+'" ></standard-pass-ont:hasSourceState>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasTargetState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.target.id+'" ></standard-pass-ont:hasTargetState>\n';

                            //Message
                            var message = service.messages.find(function (msg) {
                                return msg.messageId === edge.customAttrs.fromMessage.id;
                            });

                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:refersTo rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#'+message.messageOwlIdentifier+'" ></standard-pass-ont:refersTo>\n';

                        } else if (edge.type === 'sbd.FromFunctionStateConnectorElement') {

                            sbdElementInfo += service.twoTabs + '<rdf:type rdf:resource="&standard-pass-ont;StandardTransition" ></rdf:type>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentID rdf:datatype="&xsd;string" >SBD_edge_'+edge.id+'</standard-pass-ont:hasModelComponentID>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasModelComponentLable xml:lang="en" >'+edge.customAttrs.transitionName+'</standard-pass-ont:hasModelComponentLable>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasSourceState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.source.id+'" ></standard-pass-ont:hasSourceState>\n';
                            sbdElementInfo += service.twoTabs + '<standard-pass-ont:hasTargetState rdf:resource="http://www.imi.kit.edu/s-bpm/exampleProcess#SBD_state_'+edge.target.id+'" ></standard-pass-ont:hasTargetState>\n';

                        }

                        sbdElementInfo += service.oneTab + '</owl:NamedIndividual>\n';
                    });
                }
            });

            return sbdElementInfo;
        };

        service.init();

        return service;
    }

})();
