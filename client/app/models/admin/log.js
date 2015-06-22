import DS from 'ember-data';

var attr = DS.attr;
var Model = DS.Model;

export default Model.extend({
  level : attr("Number"),
  bsn : attr("String"),
  time : attr("MongoDate"),
  message : attr("String")
});
