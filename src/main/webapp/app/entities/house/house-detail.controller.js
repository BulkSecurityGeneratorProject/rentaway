(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('HouseDetailController', HouseDetailController);

    HouseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'House', 'Address'];

    function HouseDetailController($scope, $rootScope, $stateParams, entity, House, Address) {
        var vm = this;

        vm.house = entity;

        var unsubscribe = $rootScope.$on('rentApp:houseUpdate', function(event, result) {
            vm.house = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
