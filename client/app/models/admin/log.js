import Ember from 'ember';
import DS from 'ember-data';

var computed = Ember.computed;
var attr = DS.attr;
var Model = DS.Model;

export default Model.extend({
  level : attr("Number"),
  bsn : attr("String"),
  time : attr("MongoDate"),
  message : attr("String"),

  date : computed('time', function() {
    var time = this.get('time');
    return (time.getMonth() + 1) + "-" + time.getDate() + "-" + time.getFullYear();
  }),

  levelLabel : computed('level', function() {
    switch (this.get('level')) {
      case 1:
        return "ERROR";
      case 2:
        return "WARNING";
      case 3:
        return "INFO";
      case 4:
        return "DEBUG";
      default:
        return "UNK";
    }
  })
});
