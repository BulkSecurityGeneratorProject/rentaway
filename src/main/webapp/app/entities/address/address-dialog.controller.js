(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('AddressDialogController', AddressDialogController);

    AddressDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Address', 'Country', 'Location'];

    function AddressDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Address, Country, Location) {
        var vm = this;

        vm.address = entity;
        vm.clear = clear;
        vm.save = save;
        vm.countries = Country.query({filter: 'address-is-null'});
        $q.all([vm.address.$promise, vm.countries.$promise]).then(function() {
            if (!vm.address.country || !vm.address.country.id) {
                return $q.reject();
            }
            return Country.get({id : vm.address.country.id}).$promise;
        }).then(function(country) {
            vm.countries.push(country);
        });
        vm.locations = Location.query({filter: 'address-is-null'});
        $q.all([vm.address.$promise, vm.locations.$promise]).then(function() {
            if (!vm.address.location || !vm.address.location.id) {
                return $q.reject();
            }
            return Location.get({id : vm.address.location.id}).$promise;
        }).then(function(location) {
            vm.locations.push(location);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.address.id !== null) {
                Address.update(vm.address, onSaveSuccess, onSaveError);
            } else {
                Address.save(vm.address, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('rentApp:addressUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
