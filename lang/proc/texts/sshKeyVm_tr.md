# VM SSH kimlikleri

VM konuk kullanıcınız SSH için anahtar tabanlı kimlik doğrulama gerektiriyorsa, bunu buradan etkinleştirebilirsiniz.

Sanal makinenizin herkese açık olmadığının varsayıldığını, bu nedenle sanal makine ana sisteminin bir SSH ağ geçidi olarak kullanıldığını unutmayın.
Sonuç olarak, herhangi bir kimlik seçeneği yerel makinenize göre değil, VM ana bilgisayar sistemine göre belirtilir.
Burada belirttiğiniz herhangi bir anahtar, VM ana bilgisayarında bir dosya olarak yorumlanır.
Herhangi bir aracı kullanıyorsanız, aracının yerel makinenizde değil VM ana bilgisayar sisteminde çalışıyor olması beklenir.

### Yok

Eğer seçilirse, XPipe herhangi bir kimlik sağlamayacaktır. Bu aynı zamanda aracılar gibi harici kaynakları da devre dışı bırakır.

### Kimlik dosyası

İsteğe bağlı bir parola ile bir kimlik dosyası da belirtebilirsiniz.
Bu seçenek `ssh -i <dosya>` seçeneğine eşdeğerdir.

Bunun genel değil *özel* anahtar olması gerektiğini unutmayın.
Eğer bunu karıştırırsanız, ssh size sadece şifreli hata mesajları verecektir.

### SSH-Agent

Kimliklerinizin SSH-Agent'ta depolanması durumunda, ssh yürütülebilir dosyası, agent başlatıldığında bunları kullanabilir.
XPipe, henüz çalışmıyorsa aracı sürecini otomatik olarak başlatacaktır.

Sanal makine ana bilgisayar sisteminde aracı kurulu değilse, sanal makine ana bilgisayarına orijinal SSH bağlantısı için SSH aracısı iletmeyi etkinleştirmeniz önerilir.
Bunu, `ForwardAgent` seçeneği etkinleştirilmiş özel bir SSH bağlantısı oluşturarak yapabilirsiniz.

### GPG Agent

Kimlikleriniz örneğin bir akıllı kartta saklanıyorsa, bunları SSH istemcisine `gpg-agent` aracılığıyla sağlamayı seçebilirsiniz.
Bu seçenek, henüz etkinleştirilmemişse aracının SSH desteğini otomatik olarak etkinleştirecek ve GPG aracı arka plan programını doğru ayarlarla yeniden başlatacaktır.

### Yubikey PIV

Kimlikleriniz Yubikey'in PIV akıllı kart işlevi ile saklanıyorsa, şunları geri alabilirsiniz
yubico PIV Aracı ile birlikte gelen Yubico'nun YKCS11 kütüphanesi ile.

Bu özelliği kullanabilmek için güncel bir OpenSSH yapısına ihtiyacınız olduğunu unutmayın.

### Özel PKCS#11 kütüphanesi

Bu, OpenSSH istemcisine kimlik doğrulamasını gerçekleştirecek olan belirtilen paylaşılan kütüphane dosyasını yüklemesi talimatını verecektir.

Bu özelliği kullanabilmek için güncel bir OpenSSH yapısına ihtiyacınız olduğunu unutmayın.

### Pageant (Windows)

Windows üzerinde pageant kullanıyorsanız, XPipe önce pageant'ın çalışıp çalışmadığını kontrol edecektir.
Pageant'ın doğası gereği, pageant'a sahip olmak sizin sorumluluğunuzdadır
her seferinde eklemek istediğiniz tüm anahtarları manuel olarak belirtmeniz gerektiğinden çalışıyor.
Eğer çalışıyorsa, XPipe uygun adlandırılmış boruyu
`-oIdentityAgent=...` ssh için, herhangi bir özel yapılandırma dosyası eklemeniz gerekmez.

### Pageant (Linux ve macOS)

Kimliklerinizin pageant aracısında saklanması durumunda, aracı başlatılırsa ssh yürütülebilir dosyası bunları kullanabilir.
XPipe, henüz çalışmıyorsa aracı sürecini otomatik olarak başlatacaktır.

### Diğer dış kaynak

Bu seçenek, çalışan herhangi bir harici kimlik sağlayıcısının anahtarlarını SSH istemcisine sağlamasına izin verecektir. SSH anahtarlarınızı yönetmek için başka bir aracı veya parola yöneticisi kullanıyorsanız bu seçeneği kullanmalısınız.
