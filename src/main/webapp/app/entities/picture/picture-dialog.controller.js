(function() {
    'use strict';

    angular
        .module('springBootAngJsTemplateApp')
        .controller('PictureDialogController', PictureDialogController);

    PictureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Picture', 'Restaurant'];

    function PictureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Picture, Restaurant) {
        var vm = this;

        vm.picture = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.restaurants = Restaurant.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.picture.id !== null) {
                Picture.update(vm.picture, onSaveSuccess, onSaveError);
            } else {
                Picture.save(vm.picture, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('springBootAngJsTemplateApp:pictureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImg = function ($file, picture) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        picture.img = base64Data;
                        picture.imgContentType = $file.type;
                    });
                });
            }
        };

    }
})();
