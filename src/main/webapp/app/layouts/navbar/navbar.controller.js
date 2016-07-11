(function() {
    'use strict';

    angular
        .module('rentApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$scope', '$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'RenatawayService'];

    function NavbarController ($scope, $state, Auth, Principal, ProfileService, LoginService, RenatawayService) {
        var vm = this;

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerDisabled = response.swaggerDisabled;
        });
        
       /* RenatawayService.getRentawayInfo().then(function(response) {
        	vm.googleSearchKey = response.searchkey;
        });*/

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;
        
        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
        
    	//Google address
        $scope.result1 = '';
        $scope.options1 = null;
        $scope.details1 = '';
    }
})();
