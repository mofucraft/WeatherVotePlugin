# Weather vote plugin
Weather vote plugin は プレイヤーの投票により、晴れにするBukkitプラグインです。

・反対者が一人いた場合にはそのままの気候

・ワールド「world」のみ晴れに変更

・投票受付中には投票を開始させることはできません。（二重投票開始をしない）

・5分おきに投票ができるように

# Commands

| コマンド | エイリアス | 説明 | 権限 |
| --- | :---: | :---: | --- |
|/mvote vote| sun,day |投票を開始する|default|
|/mvote oppose| |投票に反対する|default|
|/mvote voteinfo| |現在の投票の状態を見る|default|