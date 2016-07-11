(function() {
    'use strict';

    angular
        .module('rentApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('browse', {
            parent: 'app',
            url: '/browse',
            data: {
                authorities: [],
                pageTitle: 'Browse'
            },
            views: {
                'content@': {
                    templateUrl: 'app/browse/browse.html',
                    controller: 'BrowseController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
