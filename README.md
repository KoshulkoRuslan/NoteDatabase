# NoteDatabase
Приложение Список заметок. 
При построении использовалась ахетиктура MVVM. Применена Day|Night тема MaterialDesign.
Основные технологии: 
ArchitectureComponents
Room (хранение записей)
Broadrast receiver + Work. 
Формирование отложенных уведомлений происходит с помощью AlarmManager. BroadrastReceiver отслеживает перезагрузку устройства
и запускает инициализацию повторной установки AlarmManager после перезагрузки устройства. 

