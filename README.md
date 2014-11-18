Что бы запустить программу в командной строке введите:
 
java -jar Spider.jar http://site.ru - Скачать сайт полностью 

------------------------------------------------------------

java -jar Spider.jar -d N http://site.ru
или
java -jar Spider.jar --dephth=N http://site.ru - Скачать сайт 
при этом следуя по ссылкам не более чем до уровня N, где
N = 0 - 10
Пример:
http://site.ru - нулевой уровень
http://site.ru/folder/ - первый уровень
