(function(app) {
  app.TodoComponent = TodoComponent;
  TodoComponent.annotations = [
    new ng.core.Component({
      selector: 'todo',
      templateUrl: 'app/todo/todo.component.html',
    })
  ];

  TodoComponent.parameters = [ app.DataService ];

  function TodoComponent(dataService) {
    this.todo = {};

    this.newTodo = function() {
      // Shallow clone this.todo
      var todo = Object.assign({}, this.todo);
      dataService.addTodo(todo);
    }
  }
})(window.app = window.app || {});
