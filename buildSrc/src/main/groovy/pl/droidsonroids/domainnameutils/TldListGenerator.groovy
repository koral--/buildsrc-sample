package pl.droidsonroids.domainnameutils

import com.sun.codemodel.ClassType
import com.sun.codemodel.JCodeModel

import static com.sun.codemodel.JMod.*

public class TldListGenerator {

    private static final URL IANA_TLDS_URL =
            new URL('https://data.iana.org/TLD/tlds-alpha-by-domain.txt')

    /**
     * Generates <code>pl.droidsonroids.domainnameutils.TldList</code> and places it into <code>outputDir</code>.
     * @param outputDir directory where generated sources will be written to
     * @param useSavedVersion if true then saved TLD data will be used instead of downloading it
     * from IANA's website
     */
    public static void generateTldListClass(final String outputDir, final boolean useSavedVersion) {
        def javadocConfig = new ConfigSlurper()
                .parse(TldListGenerator.class.getResource('javadoc.properties'))
        def sourceUrl = useSavedVersion ?
                TldListGenerator.class.getResource('tlds-alpha-by-domain.txt')
                : IANA_TLDS_URL

        def codeModel = new JCodeModel()

        def fqcn = TldListGenerator.class.getPackage().getName() + '.TldList'
        def tldListClass = codeModel._class(PUBLIC | FINAL, fqcn, ClassType.CLASS)
        def classJavadoc = tldListClass.javadoc()
        classJavadoc.append(javadocConfig.getProperty('classJavadoc'))

        def listStringType = codeModel.ref(List.class).narrow(codeModel.ref(String.class))
        def asListInvocation = codeModel.directClass(Arrays.class.getName()).staticInvoke('asList')

        sourceUrl.eachLine {
            if (!it.startsWith('#')) {
                asListInvocation.arg(it.toLowerCase(Locale.ENGLISH))
            } else {
                classJavadoc.append('\n<br/>').append(it.replaceFirst('#\\s+', ''))
            }
            return
        }

        def constant = codeModel
                .directClass(Collections.class.getName())
                .staticInvoke('unmodifiableList')
                .arg(asListInvocation)
        def field = tldListClass.field(PRIVATE | STATIC | FINAL, listStringType, 'TLD_LIST', constant)
        def method = tldListClass.method(PUBLIC | STATIC, listStringType, 'getTldList')

        method.javadoc()
                .append(javadocConfig.getProperty('methodJavadoc'))
                .addReturn()
                .append(javadocConfig.getProperty('methodReturnJavadoc'))
        method.body()._return(field)
        tldListClass.constructor(PRIVATE)

        def outputFile = new File(outputDir)
        if (!outputFile.isDirectory() && !outputFile.mkdirs()) {
            throw new IOException('Could not create directory: ' + outputDir)
        }
        codeModel.build(outputFile)
    }
}
