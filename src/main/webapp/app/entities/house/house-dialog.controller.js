(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('HouseDialogController', HouseDialogController);

    HouseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'House', 'Address'];

    function HouseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, House, Address) {
        var vm = this;

        vm.house = entity;
        vm.clear = clear;
        vm.save = save;
        vm.addresses = Address.query({filter: 'house-is-null'});
        $q.all([vm.house.$promise, vm.addresses.$promise]).then(function() {
            if (!vm.house.address || !vm.house.address.id) {
                return $q.reject();
            }
            return Address.get({id : vm.house.address.id}).$promise;
        }).then(function(address) {
            vm.addresses.push(address);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.house.id !== null) {
                House.update(vm.house, onSaveSuccess, onSaveError);
            } else {
                House.save(vm.house, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rentApp:houseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
