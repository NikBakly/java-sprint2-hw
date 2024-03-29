#Second sprint homework
# ТЗ: «Трекер задач».

## Задача

У задачи есть следующие свойства:

* Название, кратко описывающее суть задачи (например, «Переезд»).
* Описание, в котором раскрываются детали.
* Уникальный идентификационный номер задачи, по которому её можно будет найти.
* Статус, отображающий её прогресс. Мы будем выделять следующие этапы жизни задачи:
   1. NEW — задача только создана, но к её выполнению ещё не приступили.
   2. IN_PROGRESS — над задачей ведётся работа.
   3. DONE — задача выполнена.

Иногда для выполнения какой-нибудь масштабной задачи её лучше разбить на подзадачи (англ. subtask). Большую задачу, которая делится на подзадачи, мы будем называть эпиком (англ. epic).
Таким образом, в нашей системе задачи могут быть трёх типов: обычные задачи, эпики и подзадачи. Для них должны выполняться следующие условия:

* Для каждой подзадачи известно, в рамках какого эпика она выполняется.
* Каждый эпик знает, какие подзадачи в него входят.
* Завершение всех подзадач эпика считается завершением эпика.

## Менеджер
Реализован класс Менеджер, который будет запускаться на старте программы и управлять всеми задачами. В нём должны быть реализованы следующие функции:

1. Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
2. Методы:
    1. Получение списка всех задач.
    2. Получение списка всех эпиков.
    3. Получение списка всех подзадач определённого эпика.
    4. Получение задачи любого типа по идентификатору.
    5. Добавление новой задачи, эпика и подзадачи. Сам объект должен передаваться в качестве параметра.
    6. Обновление задачи любого типа по идентификатору. Новая версия объекта передаётся в виде параметра.
    7. Удаление ранее добавленных задач — всех и по идентификатору.
3. Управление статусами осуществляется по следующему правилу:
    1. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
    2. Для эпиков:
        * если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
        * если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
        * во всех остальных случаях статус должен быть IN_PROGRESS.


## Менеджер теперь интерфейс

1. Класс Менеджер(TaskManager) должен стать интерфейсом. В нём нужно собрать список методов, которые должны быть у любого объекта-менеджера.
2. Созданный ранее класс менеджера нужно переименовать в InMemoryTasksManager. Именно то, что менеджер хранит всю информацию в оперативной памяти, и есть его главное свойство, позволяющее эффективно управлять задачами. Внутри класса должна остаться реализация методов. При этом важно не забыть имплементировать TaskManager — в Java класс должен явно заявить, что он подходит под требования интерфейса.

## История просмотров задач
Добавьте в программу новую функциональность — нужно, чтобы трекер отображал последние просмотренные пользователем задачи. Для этого реализуйте метод history() — он должен возвращать последние 10 просмотренных задач. Просмотром будем считаться вызов у менеджера методов getSubtask() или getEpic(). От повторных просмотров избавляться не нужно.

У метода history() не будет параметров. Это значит, он формирует свой ответ, анализируя исключительно внутреннее состояние полей объекта менеджера. Подумайте, каким образом и какие данные вы запишите в поля менеджера для возможности извлекать из них историю посещений. Так как в истории отображается, к каким задачам было обращение в методах getSubtask() и getEpic(), эти данные в полях менеджера будут обновляться при вызове этих двух методов.



## Утилитарный класс
Чтобы использование программы стало более удобным, создайте утилитарный класс Managers. Вся ответственность за создание менеджера задач теперь будет лежать на этом классе: он будет сам подбирать нужную реализацию и возвращать объект правильного типа.
У Managers будет единственные метод getDefault(). При этом вызывающему неизвестен конкретный класс, только то, что объект, который возвращает getDefault(), точно менеджер, то есть реализует интерфейс TaskManager.

## Статусы задач как перечисление
Так как варианты возможных статусов у задачи ограничены, для их хранения в программе лучше завести перечисляемый тип enum.

