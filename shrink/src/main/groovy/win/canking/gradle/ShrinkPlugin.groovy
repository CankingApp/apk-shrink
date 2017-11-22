package win.canking.gradle

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import win.canking.gradle.shrink.ShrinkExtension

/**
 * Created by changxing on 2017/11/21.
 */
class ShrinkPlugin implements Plugin<Project> {
    static final String SHRINK_CONFIG = "shrinkConfig"


    @Override
    void apply(Project project) {
        Log.d("shrink apply")
        if (project.getPlugins().hasPlugin(AppPlugin)) {

            def config = project.extensions.create(SHRINK_CONFIG, ShrinkExtension)
            project.afterEvaluate {
                project.tasks.matching {
                    println it.name
                    it.name.startsWith('packageRelease')
                } each {
                    t ->
                        t.doLast {
                            println "enable" + config.enable + " file:" + config.apkPath
                            if (config.enable) {
                                Log.d("shrink start...")
                                GradleMrg.do7zipCompressApk(config.apkPath)
                                Log.d("shrink EDN")
                            }
                        }
                }
            }
        }
    }
}


