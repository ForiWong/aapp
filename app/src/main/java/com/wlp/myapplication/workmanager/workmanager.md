========================================================================
版权声明：本文为CSDN博主「八归少年」的原创文章
原文链接：https://blog.csdn.net/yang_study_first/article/details/115242553
========================================================================

1、Android应用中大部分都需要执行后台任务，因此也提供了多种解决方案，如JobScheduler、Loader等。但不合理的使用这些API，
会造成消耗大量电量。JetPack中的WorkManager为应用程序执行后台任务提供了 一个统一的解决方案。
WorkManager可以自动维护后台任务的执行时机，执行顺序，执行状态。
（1）WorkManager并不是一种新的工作线程，工作线程通常立即执行，而WorkManager不能保证任务被及时执行。
（2）任务一定会被执行
WorkManager能保证任务一定会被执行，即使应用程序不在运行中，甚至是在设备重启后，任务仍然会在适当的时刻执行，这是因为
WorkManager有自己的数据库，任务的所有信息和数据都保存在数据库中。
注： WorkManager宣称能够保证任务得到执行，但是在非Android原生系统的设备不一定可以。
（3）针对不需要及时完成的任务
例如，发送应用日志、同步应用数据，备份应用数据等。这些任务不需要立刻被执行。

2、任务构建
项目开发中有这样的需求；任务A执行完再执行C，任务B执行完再执行D；任务A、B、C、D都执行完成后再执行E。那么可以利用
WorkManager.beginWith().then().then...enqueue()的方式构建任务链。

WorkContinuation left,right;
left=WorkManager.getInstance(this).beginWith(A).then(C);
right=WorkManager.getInstance(this).beginWith(B).then(D);
WorkContinuation.combine(Arrays.asList(left,right)).then(E).enqueue();

3、状态通知
WorkManager的每个任务都有以下几种状态：
BLOCKED表示任务被阻塞；ENQUEUED表示刚加入执行队列；RUNNING表示任务正在被执行，其中SUCCESSED、CANCELED、FAILED表示
任务执行成功、取消执行和执行失败；FINISHED表示任务结束。

4、任务控制
        //通过ID取消单个任务
        WorkManager.getInstance(this).cancelWorkById(UUID);
        //通过tag取消所有任务
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        //通过任务名取消唯一任务
        WorkManager.getInstance(this).cancelUniqueWork(workName);
        //取消所有任务
        WorkManager.getInstance(this).cancelAllWork();

5、类关系
Worker
    任务的执行者，是一个抽象类,需要继承它实现要执行的任务。
WorkRequest
    指定让哪个Woker执行任务,指定执行的环境,执行的顺序等。要使用它的子类OneTimeWorkRequest或PeriodicWorkRequest。
WokManager
    管理任务请求和任务队列,发起的WorkRequest会进入它的任务队列。
WorkStatus
    包含有任务的状态和任务的信息,以 LiveData 的形式提供给观察者。

6、使用
（1）创建任务
使用Worker类定义任务，复写doWork()，在doWork()里执行耗时任务。

（2）输入参数
任务执行过程中可能需要传递一些参数，通过{key,value}的形式添加到Data中。
Data inputData = getInputData();
在Data源码中发现，Data传递的数据最大不能超过10KB，因此Data只能传递一些小的基本类型的数据。

（3）使用WorkRequest配置任务
WorkRequest 本身是抽象基类。该类有两个实现类，可用于创建 OneTimeWorkRequest 和 PeriodicWorkRequest 请求。
OneTimeWorkRequest 适用于调度非重复性工作，而 PeriodicWorkRequest 则更适合调度以一定间隔重复执行的工作。

OneTimeWorkRequest
对于无需额外配置的简单工作，请使用静态方法 from。
WorkRequest myWorkRequest = OneTimeWorkRequest.from(MyWork.class);

对于比较复杂的任务，可以使用构建器。
WorkRequest uploadWorkRequest =  new OneTimeWorkRequest.Builder(MyWork.class)
           // Additional configuration
           .build();

PeriodicWorkRequest
应用有时可能需要定期运行某些工作。例如，需要定期备份数据、或者定期上传日志到服务器。
 //任务的运行时间间隔定为一小时
PeriodicWorkRequest saveRequest = new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class, 1, TimeUnit.HOURS)
                // Constraints
                .build();

时间间隔定义为两次重复执行之间的最短时间。工作器的确切执行时间取决于您在 WorkRequest 对象中设置的约束以及系统执行的优化。
需要注意的是定义的最短重复间隔是 15 分钟，在源码中也可以看到。

如果对任务的执行实际比较敏感，可以将PeriodicWorkRequest 配置为在每个时间间隔的灵活时间段内执行。

//每小时的最后15分钟内执行的定期任务  
WorkRequest saveRequest = new PeriodicWorkRequest.Builder(SaveImageToFileWorker.class,
                     1, TimeUnit.HOURS,
                    15, TimeUnit.MINUTES)
                .build();

定义的最短灵活间隔是5分钟，在源码中也可以看到。

（4）约束条件
在执行任务之前，我们可以对任务添加各种约束，使其满足约束条件后才执行任务。通过Contraints.Builder()  
创建Constraints 实例。

Constraints constraints = new Constraints.Builder()
                //设备存储空间充足的时候 才能执行 ,>15%
                .setRequiresStorageNotLow(true)
                //必须在执行的网络条件下才能好执行,不计流量 ,wifi
                .setRequiredNetworkType(NetworkType.UNMETERED)
                //设备的充电量充足的才能执行 >15%
                .setRequiresBatteryNotLow(true)
                //只有设备在充电的情况下 才能允许执行
                .setRequiresCharging(true)
                //只有设备在空闲的情况下才能被执行 比如息屏，cpu利用率不高
                .setRequiresDeviceIdle(true)
                //workmanager利用contentObserver监控传递进来的这个uri对应的内容是否发生变化,当且仅当它发生变化了
                //设置从content变化到被执行中间的延迟时间，如果在这期间。content发生了变化，延迟时间会被重新计算
                .setTriggerContentUpdateDelay(Duration.ZERO)
                //设置从content变化到被执行中间的最大延迟时间
                .setTriggerContentMaxDelay(Duration.ZERO).build();

再将Constraint实例设置到OneTimeWorkRequest中去。

OneTimeWorkRequest request = new OneTimeWorkRequest
                .Builder(UploadFileWorker.class)
                .setInputData(inputData)
                .setConstraints(constraints)
                //对任务添加标记
                .addTag("tag")
    			//设置延迟执行时间
                .setInitialDelay(10,TimeUnit.SECONDS)
                //设置一个拦截器，在任务执行之前 可以做一次拦截，去修改入参的数据然后返回新的数据交由worker使用
                .setInputMerger(null)
                //当一个任务被调度失败后，所要采取的重试策略，可以通过BackoffPolicy来执行具体的策略
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                //任务被调度执行的延迟时间
                .setInitialDelay(10, TimeUnit.SECONDS)
                //设置该任务尝试执行的最大次数
                .setInitialRunAttemptCount(2)
                //设置这个任务开始执行的时间
                //System.currentTimeMillis()
                .setPeriodStartTime(0, TimeUnit.SECONDS)
                //指定该任务被调度的时间
                .setScheduleRequestedAt(0, TimeUnit.SECONDS)
                //当一个任务执行状态变成finish时，又没有后续的观察者来消费这个结果，那么workmanager会在
                //内存中保留一段时间的该任务的结果。超过这个时间，这个结果就会被存储到数据库中
                //下次想要查询该任务的结果时，会触发workmanager的数据库查询操作，可以通过uuid来查询任务的状态
                .keepResultsForAtLeast(10, TimeUnit.SECONDS)
                .build();

fileUploadUUID = request.getId();

（5）
将任务提交给系统
 		WorkContinuation workContinuation = WorkManager.getInstance(this).beginWith(workRequests);
        workContinuation.enqueue();

（6）
监听执行状态和结果
任务提交给系统后，可以通过WorkInfo获知任务的状态。WorkInfo包含任务的id、tag和worker对象传递过来的outputData，  
以及任务当前的状态。有三种方式可以得到WorkInfo对象。

 WorkManager.getInstance(this).getWorkInfoById(UUID);
 WorkManager.getInstance(this).getWorkInfosByTag(tag);
 WorkManager.getInstance(this).getWorkInfosForUniqueWork(workName);

实时获取任务的状态也有三种方法。

 WorkManager.getInstance(this).getWorkInfoByIdLiveData(UUID);
 WorkManager.getInstance(this).getWorkInfosByTag(tag);
 WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(workName);

通过LiveData，我们便可以在任务状态发生变化时收到通知。

 WorkManager.getInstance(this).getWorkInfoByIdLiveData(fileUploadUUID).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                WorkInfo.State state = workInfo.getState();
                if (state == WorkInfo.State.FAILED) {
                    //失败
                } else if (state == WorkInfo.State.SUCCEEDED) {
                    //成功
                }
                workInfo.getProgress();
                workInfo.getId();

            }
        });

 workContinuation.getWorkInfosLiveData().observe(PublishActivity.this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
             for (WorkInfo workInfo : workInfos) {
                    WorkInfo.State state = workInfo.getState();
                    Data outputData = workInfo.getOutputData();
                    UUID uuid = workInfo.getId();
             }
 });


========================================================================
作者：九心_
链接：https://www.jianshu.com/p/68e720b8a939
========================================================================
强大的生命力，就算重启手机，杀掉进程重启后，任务依然会被执行。
当应用正在运行时，它会在当前的进程中启用一个子线程执行。应用没有运行的情况下启用，它则会自己选择一种合适的方式在后台运行。

保活?
这里引入一个思考，既然 WorkManager 的生命力这么强，还可以实现定时任务，那能不能让我们的应用生命力也这么强？换句话说，能  
不能用它来保活?
要是上面有细看的话，你应该已经发现这几点了：
定时任务有最小间隔时间的限制，是 15 分钟
只有程序运行时，任务才会得到执行
无法拉起 Activity
总之，用 WorkManager 保活是不可能了，这辈子都不可能保活了

选择适合自己的Worker
谷歌提供了四种Worker给我们使用，分别为：自动运行在后台线程的Worker、结合协程的CoroutineWorker、  
结合RxJava2的RxWorker和以上三个类的基类的ListenableWorker。

由于本文使用的Kotlin，故打算简单的介绍CoroutineWorker，其他的可以自行探索。
