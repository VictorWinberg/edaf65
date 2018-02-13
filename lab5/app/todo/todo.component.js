(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: 'todo',
      templateUrl: 'app/todo/todo.component.html',
    })
  ];

  TodoComponent.parameters = [ ng.router.ActivatedRoute, ng.router.Router, app.DataService ];

  function TodoComponent(route, router, dataService) {
    this.todo = {text: "", done: false};
    this.dataService = dataService;

    this.newTodo = function() {
      // Shallow clone this.todo
      var todo = Object.assign({}, this.todo);
      dataService.addTodo(todo);
      router.navigate(['/list']);
    }

    this.updateTodo = function() {
      // Shallow clone this.todo
      var todo = Object.assign({}, this.todo);
      dataService.editTodo(todo);
      router.navigate(['/list']);
    }

    route.params.subscribe((params) => {
      var id = params['id']
      tmp = dataService.getTODOs()[id];
      if(tmp) {
        this.todo = Object.assign({ id }, tmp);
      }
    });
  }
})(window.app = window.app || {});
