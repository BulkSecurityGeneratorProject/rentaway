(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('AddressDetailController', AddressDetailController);

    AddressDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Address', 'Country', 'Location'];

    function AddressDetailController($scope, $rootScope, $stateParams, entity, Address, Country, Location) {
        var vm = this;

        vm.address = entity;

        var unsubscribe = $rootScope.$on('rentApp:addressUpdate', function(event, result) {
            vm.address = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
