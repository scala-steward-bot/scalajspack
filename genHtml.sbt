val genHtmlLocal = TaskKey[Unit]("genHtmlLocal")

genHtmlLocal := {
  val js = "./js/target/scala-2.11/scalajspack-fastopt.js"
  val html = gen(js)
  IO.write(file("index.html"), html)
}

TaskKey[Unit]("genAndCheckHtml") := {
  genHtmlLocal.value
  val diff = "git diff".!!
  if(diff.nonEmpty){
    sys.error("Working directory is dirty!\n" + diff)
  }
}

TaskKey[Unit]("genHtmlPublish") := {
  val js = "./scalajspack.js"
  val html = gen(js)
  IO.write(file("index.html"), html)
}

def gen(js: String) = s"""<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>scalajspack - JSON to MessagePack converter powered by scala-js</title>
    <script type="text/javascript" src="${js}"></script>
    <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
  </head>
  <body>
    <p><a target="_brank" href="https://github.com/xuwei-k/scalajspack">https://github.com/xuwei-k/scalajspack</a></p>
    <div>
      <textarea id="input_js" style="height: 200px; width: 400px;">{"aaa": [true, 1000, null]}</textarea>
      <button id="convert_button">convert</button>
    </div>
    <div>
      <pre id="output_msgpack"></pre>
      <pre id="error" style="color: red;"></pre>
    </div>
  </body>

<script type="text/javascript">
$$(function(){
  var run = function(){
    try{
      var a = new scalajspack.Main
      var r = a.convert($$("#input_js").val());
      $$("#output_msgpack").text(r); 
      $$("#error").text("");
    }catch(e){
      $$("#error").text(e);
      $$("#output_msgpack").text("");
    }
  };

  $$("#input_js").keyup(function(event){
    if(event.keyCode == 13){
      run();
    }
  });

  $$("#convert_button").click(function(){
    run();
  });

  $$(document).ready(function(){
    run();
  });
});
</script>

</html>
"""
